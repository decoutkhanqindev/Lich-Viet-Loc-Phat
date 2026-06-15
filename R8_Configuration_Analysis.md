# R8 Configuration Analysis — Lịch Việt Lộc Phát

## R8 Configuration

| Setting | Status |
|---|---|
| `isMinifyEnabled = true` | ✅ Enabled |
| `isShrinkResources = true` | ✅ Enabled |
| `proguard-android-optimize.txt` | ✅ Correct optimized file |
| R8 Full Mode (`android.enableR8.fullMode=false`) | ✅ Not present — Full Mode active (AGP 9.2.1) |
| Optimized resource shrinking | ✅ Enabled by default (AGP 9.0+) |

---

## Libraries Check — Redundant Rules

The following rules target libraries that bundle their own consumer ProGuard rules. These rules prevent R8 from optimizing library internals and must be removed.

### Koin 4.2.1
```proguard
-keep class org.koin.** { *; }
-keepclassmembers class org.koin.** { *; }
-dontwarn org.koin.**
```
**Action: Remove all 3 rules.** Koin 4.x bundles its own consumer rules. These broad rules prevent R8 from shrinking unused Koin internals.

### Kotlin Coroutines 1.11.0
```proguard
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-dontwarn kotlinx.coroutines.**
```
**Action: Remove all 3 rules.** Coroutines v1.7.0+ bundles consumer rules that handle these dispatcher and exception handler classes. Adding them manually is explicitly redundant at v1.11.0.

### Glance 1.1.1
```proguard
-keep class androidx.glance.** { *; }
-dontwarn androidx.glance.**
```
**Action: Remove both rules.** AndroidX Glance bundles its own consumer ProGuard rules. This broad `{ *; }` rule prevents R8 from shrinking all unused Glance internals.

### Jetpack Compose
```proguard
-keep class androidx.compose.runtime.** { *; }
-dontwarn androidx.compose.**
```
**Action: Remove both rules.** Compose bundles its own consumer rules. Manually keeping `androidx.compose.runtime.**` prevents R8 from removing unused Compose runtime internals.

### Lottie 6.6.6
```proguard
-keep class com.airbnb.lottie.** { *; }
-dontwarn com.airbnb.lottie.**
```
**Action: Remove both rules.** Lottie 6.x bundles its own consumer ProGuard rules. This broad package keep prevents any optimization of the Lottie library.

---

## Subsuming Rules

```proguard
-keep @kotlinx.serialization.Serializable class * { *; }   ← (A)
-keep,includedescriptorclasses class com.decoutkhanqindev.lich_viet_loc_phat.**$$serializer { *; }   ← (B)
-keepclassmembers class com.decoutkhanqindev.lich_viet_loc_phat.** { *** Companion; }   ← (C)
-keepclasseswithmembers class com.decoutkhanqindev.lich_viet_loc_phat.** { kotlinx.serialization.KSerializer serializer(...); }   ← (D)
```

Rule **(A)** subsumes **(B)**, **(C)**, and **(D)**. Because (A) keeps **all members** of every `@Serializable` class in the entire classpath, rules (B), (C), and (D) have no additional effect while (A) is present.

---

## Impact Analysis — Remaining Rules

### 1. `-keep @kotlinx.serialization.Serializable class * { *; }`
Applies to every `@Serializable` class across the entire classpath — not limited to the app's own package. Prevents R8 from optimizing all members of all serializable classes.

**Action: Remove.** `kotlinx.serialization` 1.5.0+ bundles conditional consumer rules that precisely keep serializer companion methods only when they are referenced. This broad rule is redundant and significantly more harmful to optimization.

### 2. `-keepclassmembers class * { @kotlinx.serialization.Serializable *; }`
`@Serializable` is a class-level annotation — it cannot annotate individual members. This rule matches nothing and has no effect.

**Action: Remove.** This rule is a no-op.

### 3. `-keep,includedescriptorclasses class com.decoutkhanqindev.lich_viet_loc_phat.**$$serializer { *; }`
Keeps all generated `$$Serializer` companion classes for the app's `@Serializable` destinations.

**Action: Remove.** The kotlinx.serialization consumer rules include a conditional rule (`-if @kotlinx.serialization.Serializable class **`) that already keeps these generated serializer classes when accessed. Covered by the library.

### 4. `-keepclassmembers class com.decoutkhanqindev.lich_viet_loc_phat.** { *** Companion; }`
Keeps all `Companion` objects for every class in the entire app package — not limited to `@Serializable` classes.

**Action: Remove.** This is broader than necessary. Rule (D) below already conditionally keeps Companions that have `serializer()` methods specifically. All other Companions R8 can safely analyze.

### 5. `-keepclasseswithmembers class com.decoutkhanqindev.lich_viet_loc_phat.** { kotlinx.serialization.KSerializer serializer(...); }`
A conditional rule that only keeps classes within the app package that expose a `serializer()` returning `KSerializer`. Targets only `TodayDestination`, `SplashDestination`, etc.

**Action: Keep as safety net.** This is the most targeted of the serialization rules. Navigation 3 (1.1.2) is a new library; until its consumer ProGuard rules are mature, this conditional rule protects NavKey serialization without blocking unrelated optimizations.

### 6. `-keepattributes *Annotation*, InnerClasses`
Preserves annotation metadata and inner class information at the bytecode level. Required for `kotlinx.serialization` to correctly identify `@Serializable` classes and for Compose's `@Immutable`/`@Stable` annotations to remain traceable.

**Action: Keep.**

### 7. `-keep class * implements androidx.navigation3.runtime.NavKey { *; }`
Keeps all classes implementing `NavKey` with all members. Affects: `SplashDestination`, `MainDestination`, `TodayDestination`, `CalendarDestination`, `SettingsDestination`.

**Action: Refine.** Keeping `{ *; }` prevents R8 from removing unused methods on these destination classes. A narrower rule preserving only what Navigation 3 needs for serialization:

```proguard
-keepclassmembers @kotlinx.serialization.Serializable class * implements androidx.navigation3.runtime.NavKey {
    static ** Companion;
    static ** INSTANCE;
    static kotlinx.serialization.KSerializer serializer(...);
}
```

This keeps:
- `INSTANCE` for `data object` destinations
- `Companion` + `serializer()` for `data class` destinations (e.g., `TodayDestination`)
- No other members are retained unnecessarily

### 8. `-assumenosideeffects class timber.log.Timber { ... }`
Instructs R8 to remove all `Timber.v()`, `Timber.d()`, and `Timber.i()` call sites in release builds.

**Action: Keep.** Correct and targeted.

---

## Action Summary (ordered by impact)

| Rule | Action |
|---|---|
| `-keep @kotlinx.serialization.Serializable class * { *; }` | **Remove** — subsumed/library-covered, blocks all @Serializable optimization |
| `-keep class androidx.compose.runtime.** { *; }` | **Remove** — library-redundant, broad package keep |
| `-keep class androidx.glance.** { *; }` | **Remove** — library-redundant, broad package keep |
| `-keep class com.airbnb.lottie.** { *; }` | **Remove** — library-redundant, broad package keep |
| `-keep class org.koin.** { *; }` | **Remove** — library-redundant |
| `-keepclassmembers class org.koin.** { *; }` | **Remove** — library-redundant |
| `-keepclassmembers class com.decoutkhanqindev.lich_viet_loc_phat.** { *** Companion; }` | **Remove** — subsumed, too broad |
| `-keep,includedescriptorclasses class ...$$serializer { *; }` | **Remove** — library-covered |
| `-keepclassmembers class * { @kotlinx.serialization.Serializable *; }` | **Remove** — no-op rule |
| `-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}` | **Remove** — library-redundant |
| `-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}` | **Remove** — library-redundant |
| `3× -dontwarn` (koin, coroutines, glance, compose, lottie) | **Remove** — unnecessary with current library versions |
| `-keep class * implements androidx.navigation3.runtime.NavKey { *; }` | **Refine** — narrow to serialization members only |
| `-keepclasseswithmembers ... { KSerializer serializer(...); }` | **Keep** — targeted conditional rule |
| `-keepattributes *Annotation*, InnerClasses` | **Keep** — required |
| `-assumenosideeffects class timber.log.Timber { ... }` | **Keep** — correct |

---

## Testing Recommendation

After applying the changes above, run the release build and test the following flows with UI Automator, as they are directly affected by the removed/refined keep rules:

- **Navigation** — all tab transitions (Today → Calendar → Settings), Splash → Main flow, backstack restoration after process death
- **Serialization** — TodayDestination with day/month/year parameters passing correctly between screens
- **Glance Widget** — calendar widget rendering and tap actions
- **Lottie** — splash screen animation loads correctly in release build
- **AdMob** — banner ads load on both splash and home screens
