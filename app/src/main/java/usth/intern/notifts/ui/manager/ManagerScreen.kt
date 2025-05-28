package usth.intern.notifts.ui.manager

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import usth.intern.notifts.R
import usth.intern.notifts.data.db.Notification
import usth.intern.notifts.ui.manager.components.Date
import usth.intern.notifts.ui.manager.components.NotificationCard
import usth.intern.notifts.ui.manager.components.OptionFilterDialog
import usth.intern.notifts.ui.manager.uistate.AppFilterUiState
import usth.intern.notifts.ui.manager.uistate.CategoryFilterUiState
import usth.intern.notifts.ui.manager.uistate.DateFilterUiState
import usth.intern.notifts.ui.manager.uistate.KeywordsSearchBarUiState
import usth.intern.notifts.ui.theme.anon
import usth.intern.notifts.ui.theme.green
import usth.intern.notifts.ui.theme.veiledSpotlight
import usth.intern.notifts.ui.theme.white
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ManagerScreen(
    modifier: Modifier = Modifier,
) {
    val managerViewModel: ManagerViewModel = hiltViewModel<ManagerViewModel>()

    val uiState by managerViewModel.uiState.collectAsState()
    val keywordsSearchBarUiState by managerViewModel.keywordsSearchBarUiState.collectAsState()
    val appFilterUiState by managerViewModel.appFilterUiState.collectAsState()
    val categoryFilterUiState by managerViewModel.categoryFilterUiState.collectAsState()
    val dateFilterUiState by managerViewModel.dateFilterUiState.collectAsState()

    val focusManager = LocalFocusManager.current

    Column (
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
            }
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
        ) {
            KeywordsSearchBar(
                keywordsSearchBarUiState = keywordsSearchBarUiState,
                modifier = Modifier.weight(0.85f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            FilterDialog(
                appFilterUiState = appFilterUiState,
                categoryFilterUiState = categoryFilterUiState,
                dateFilterUiState = dateFilterUiState,
                modifier = Modifier.weight(0.15f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        NotificationCardList(
            uiState.notificationList,
            isRefreshing = uiState.isRefreshing,
            onRefresh = { managerViewModel.onReload() }
        )
    }
}

@Composable
fun KeywordsSearchBar(
    keywordsSearchBarUiState: KeywordsSearchBarUiState,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val focusState = remember { mutableStateOf(false) }

    TextField(
        value = keywordsSearchBarUiState.query,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = white,
            unfocusedContainerColor = white,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        shape = RoundedCornerShape(12.dp),
        onValueChange = keywordsSearchBarUiState.onQueryChange,
        placeholder = {
            Text(
                text = stringResource(R.string.text_search),
                color = veiledSpotlight
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = stringResource(R.string.text_search_icon),
                tint = anon
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                keywordsSearchBarUiState.onSearch(keywordsSearchBarUiState.query)
                focusManager.clearFocus()
                keyboardController?.hide()
            }
        ),
        textStyle = LocalTextStyle.current.copy(lineHeight = 20.sp),
        singleLine = true,
        modifier = modifier
            .fillMaxWidth(0.85f)
            .focusRequester(focusRequester = focusRequester)
            .onFocusChanged { focusState.value = it.isFocused }
            .focusable()
    )
}

@Composable
fun FilterDialog(
    appFilterUiState: AppFilterUiState,
    categoryFilterUiState: CategoryFilterUiState,
    dateFilterUiState: DateFilterUiState,
    modifier: Modifier = Modifier,
) {
    var filterExpanded by remember { mutableStateOf(false) }

    Box {
        Button(
            onClick = { filterExpanded = !filterExpanded },
            elevation = null,
            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = green
            ),
        ) {
            Icon(
                painter = painterResource(R.drawable.filter),
                contentDescription = stringResource(R.string.text_filter_icon),
                tint = white,
                modifier = Modifier.size(20.dp, 20.dp)
            )
        }
        DropdownMenu(
            expanded = filterExpanded,
            onDismissRequest = { filterExpanded = false },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.clip(RoundedCornerShape(10.dp))
        ) {
            DropdownMenuItem(
                text = { Text("Apps") },
                onClick = appFilterUiState.onClickAppFilterButton
            )
            DropdownMenuItem(
                text = { Text("Category") },
                onClick = categoryFilterUiState.onClickCategoryFilterButton
            )
            DropdownMenuItem(
                text = { Text("Date") },
                onClick = dateFilterUiState.onClickDateFilterButton
            )
            DropdownMenuItem(
                text = { Text("Clear") },
                onClick = {/*TODO: Implement the function of Clear button */}
            )
        }

        when {
            appFilterUiState.appFilterDialogIsShown -> OptionFilterDialog(
                optionList = appFilterUiState.appList,
                onDismissRequest = appFilterUiState.onDismissAppFilterDialog,
                updateSelection = { appFilterUiState.updateAppFilterSelections(it) },
                onCancel = { appFilterUiState.onCancelAppFilter() },
                onConfirm = { appFilterUiState.onConfirmAppFilter() }
            )
            categoryFilterUiState.categoryFilterDialogIsShown -> OptionFilterDialog(
                optionList = categoryFilterUiState.categoryList,
                onDismissRequest = categoryFilterUiState.onDismissCategoryFilterDialog,
                updateSelection = { categoryFilterUiState.updateCategoryFilterSelections(it) },
                onCancel = categoryFilterUiState.onCancelCategoryFilter,
                onConfirm = categoryFilterUiState.onConfirmCategoryFilter
            )
            dateFilterUiState.dateFilterDialogIsShown -> Date(
                onDateRangeSelected = { dateFilterUiState.onDateRangeSelected(it) },
                onDismiss= { dateFilterUiState.onDismissDateFilterDialog() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationCardList(
    notificationList: List<Notification>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
        ) {
            items (notificationList){ notification ->
                val dateString = formatter.format(notification.timestamp)

                NotificationCard(
                    packageName = notification.packageName,
                    title = notification.title,
                    text = notification.text,
                    date = dateString,
                )
                Spacer(modifier = modifier.height(10.dp))
            }
        }
    }
}