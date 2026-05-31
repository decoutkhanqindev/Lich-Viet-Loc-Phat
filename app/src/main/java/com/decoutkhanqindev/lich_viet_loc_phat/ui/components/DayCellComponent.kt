package com.decoutkhanqindev.lich_viet_loc_phat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.lich_viet_loc_phat.theme.GlassBorderStrong
import com.decoutkhanqindev.lich_viet_loc_phat.theme.GoldAccent
import com.decoutkhanqindev.lich_viet_loc_phat.theme.HolidayDot
import com.decoutkhanqindev.lich_viet_loc_phat.theme.IvoryWhite
import com.decoutkhanqindev.lich_viet_loc_phat.ui.model.DayCellUiModel

@Composable
fun DayCellComponent(
    cell: DayCellUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val hasIndicator = remember(cell.lunar.day, cell.holiday) {
        cell.holiday != null || cell.lunar.day == 1 || cell.lunar.day == 15
    }

    val solarTextColor = remember(cell.isCurrentMonth, cell.isToday) {
        when {
            !cell.isCurrentMonth -> IvoryWhite.copy(alpha = 0.3f)
            cell.isToday -> Color.Black
            else -> IvoryWhite
        }
    }
    val lunarTextColor = remember(cell.isCurrentMonth, cell.isToday) {
        when {
            !cell.isCurrentMonth -> IvoryWhite.copy(alpha = 0.25f)
            cell.isToday -> Color.Black.copy(alpha = 0.7f)
            else -> IvoryWhite.copy(alpha = 0.7f)
        }
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .then(
                if (cell.isSelected && !cell.isToday)
                    Modifier.border(1.5.dp, GlassBorderStrong, RoundedCornerShape(8.dp))
                else Modifier
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        // Today: gold circle background
        if (cell.isToday) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        Brush.radialGradient(listOf(GoldAccent, GoldAccent.copy(alpha = 0.7f))),
                        CircleShape,
                    ),
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${cell.solar.day}",
                color = solarTextColor,
                fontSize = 15.sp,
                fontWeight = if (cell.isToday) FontWeight.Bold else FontWeight.Normal,
                textAlign = TextAlign.Center,
            )
            Text(
                text = if (cell.lunar.day == 1) "1/${cell.lunar.month}" else "${cell.lunar.day}",
                color = lunarTextColor,
                fontSize = 9.sp,
                textAlign = TextAlign.Center,
            )
        }

        // Dot indicator — holiday / rằm / mùng một
        if (hasIndicator && cell.isCurrentMonth) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 2.dp)
                    .size(4.dp)
                    .background(
                        if (cell.holiday != null) HolidayDot else GoldAccent,
                        CircleShape,
                    ),
            )
        }
    }
}
