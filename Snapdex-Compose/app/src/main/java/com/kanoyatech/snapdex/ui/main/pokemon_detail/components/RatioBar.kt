package com.kanoyatech.snapdex.ui.main.pokemon_detail.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kanoyatech.snapdex.R
import com.kanoyatech.snapdex.domain.units.Percentage
import com.kanoyatech.snapdex.domain.units.percent
import com.kanoyatech.snapdex.theme.AppTheme
import com.kanoyatech.snapdex.theme.SnapdexTheme
import com.kanoyatech.snapdex.ui.utils.formatted


@Composable
fun RatioBar(
    ratio: Percentage,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row {
            Box(
                modifier = Modifier
                    .height(8.dp)
                    .weight(ratio.toFloat())
                    .clip(RoundedCornerShape(topStart = 49.dp, bottomStart = 49.dp))
                    .background(RatioBarColors.maleColor)
            )

            Box(
                modifier = Modifier
                    .height(8.dp)
                    .weight(1f - ratio.toFloat())
                    .clip(RoundedCornerShape(topEnd = 49.dp, bottomEnd = 49.dp))
                    .background(RatioBarColors.femaleColor)
            )
        }

        Row {
            Label(R.drawable.icon_male, ratio)
            Spacer(modifier = Modifier.weight(1f))
            Label(R.drawable.icon_female, 100.0.percent - ratio)
        }
    }
}

@Composable
private fun Label(
    @DrawableRes imageId: Int,
    value: Percentage
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = imageId),
            contentDescription = null,
            tint = RatioBarColors.labelColor,
            modifier = Modifier
                .size(12.dp)
        )

        Text(
            text = value.formatted(),
            //style = SnapdexTheme.typography.labelMedium,
            color = RatioBarColors.labelColor
        )
    }
}

object RatioBarColors {
    val maleColor: Color @Composable get() = SnapdexTheme.colorScheme.primary
    val femaleColor: Color @Composable get() = SnapdexTheme.colorScheme.primary
    val labelColor: Color @Composable get() = SnapdexTheme.colorScheme.primary
}

@Preview(showBackground = true)
@Composable
private fun RatioBarPreview() {
    AppTheme {
        RatioBar(
            ratio = 87.5.percent
        )
    }
}