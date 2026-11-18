package ru.nto.guestmanagement.mobile.task.a1

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import ru.nto.guestmanagement.mobile.R
import ru.nto.guestmanagement.mobile.model.PassDetails
import ru.nto.guestmanagement.mobile.ui.TestIds
import java.time.format.DateTimeFormatter

private val DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy")
private val TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm")

private fun entriesLimitStringRes(count: Int): Int {
    val lastTwo = count % 100
    if (lastTwo in 11..14) return R.string.pass_entries_limit_many
    return when (count % 10) {
        1 -> R.string.pass_entries_limit_one
        2, 3, 4 -> R.string.pass_entries_limit_few
        else -> R.string.pass_entries_limit_many
    }
}

@Composable
fun PassHolderCard(pass: PassDetails, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = pass.visitor.fullName,
            modifier = Modifier.testTag(TestIds.PASS_HOLDER),
        )

        val organization = pass.visitor.organization
        if (!organization.isNullOrBlank()) {
            Text(
                text = organization,
                modifier = Modifier.testTag(TestIds.PASS_ORGANIZATION),
            )
        }

        val dateFrom = pass.validFrom.format(DATE_FORMAT)
        val dateTo = pass.validTo.format(DATE_FORMAT)
        val timeFrom = pass.validFrom.format(TIME_FORMAT)
        val timeTo = pass.validTo.format(TIME_FORMAT)
        val validPeriod = if (pass.validFrom.toLocalDate() == pass.validTo.toLocalDate()) {
            stringResource(R.string.pass_valid_same_day, dateFrom, timeFrom, timeTo)
        } else {
            stringResource(R.string.pass_valid_range, "$dateFrom $timeFrom", "$dateTo $timeTo")
        }
        Text(
            text = validPeriod,
            modifier = Modifier.testTag(TestIds.PASS_VALID_PERIOD),
        )

        val limitText = if (pass.maxEntries == 1) {
            stringResource(R.string.pass_single_entry)
        } else {
            stringResource(entriesLimitStringRes(pass.maxEntries), pass.maxEntries)
        }
        Text(
            text = limitText,
            modifier = Modifier.testTag(TestIds.PASS_ENTRIES_LIMIT),
        )
    }
}
