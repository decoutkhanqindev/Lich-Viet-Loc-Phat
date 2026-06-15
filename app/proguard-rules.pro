# Room — generated _Impl classes loaded via reflection (WorkManager transitive dep)
-keep class * extends androidx.room.RoomDatabase { *; }

# Timber — xoá log v/d/i ở release
-assumenosideeffects class timber.log.Timber {
    public static void v(...);
    public static void d(...);
    public static void i(...);
}
