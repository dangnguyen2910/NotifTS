package usth.intern.notifts.ui.manager

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import usth.intern.notifts.R
import usth.intern.notifts.data.db.Notification
import usth.intern.notifts.ui.manager.components.Apps
import usth.intern.notifts.ui.manager.components.Category
import usth.intern.notifts.ui.manager.components.Date
import usth.intern.notifts.ui.manager.components.NotificationCard
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ManagerScreen(
    modifier: Modifier = Modifier,
) {
    val managerViewModel: ManagerViewModel = hiltViewModel<ManagerViewModel>()
    val uiState by managerViewModel.uiState.collectAsState()

    ManagerContent(
        notificationList = uiState.notificationList,
        onRefresh = { managerViewModel.onReload() },
        isRefreshing = uiState.isRefreshing,
        // Search related
        query = uiState.query,
        onTypingSearch = { managerViewModel.onTypingSearch(it) },
        onEnterSearch = { managerViewModel.onEnterSearch(it) },
        // Filter related
        // App filter
        onClickAppFilterButton = { managerViewModel.onClickAppFilterButton() },
        appList = uiState.appList,
        appFilterDialogIsShown = uiState.appFilterDialogIsShown,
        onDismissAppFilterDialog = { managerViewModel.onDismissAppFilterDialog() },
        updateAppFilterSelections = { managerViewModel.updateAppFilterSelections(it) },
        onConfirmAppFilter = { managerViewModel.onConfirmAppFilter() },
        onCancelAppFilter = { managerViewModel.onCancelAppFilter() },
        // Category filter
        onClickCategoryFilterButton = { managerViewModel.onClickCategoryFilterButton() },
        categoryList = uiState.categoryList,
        categoryFilterDialogIsShown = uiState.categoryFilterDialogIsShown,
        onDismissCategoryFilterDialog = { managerViewModel.onDismissCategoryFilterDialog() },
        updateCategoryFilterSelections = { managerViewModel.updateCategoryFilterSelections(it) },
        onConfirmCategoryFilter = { managerViewModel.onConfirmCategoryFilter() },
        onCancelCategoryFilter = { managerViewModel.onCancelCategoryFilter() },
        // Date filter
        onClickDateFilterButton = { managerViewModel.onClickDateFilterButton() },
        dateFilterDialogIsShown = uiState.dateFilterDialogIsShown,
        onDismissDateFilterDialog = { managerViewModel.onDismissDateFilterDialog() },
        onDateRangeSelected = { managerViewModel.onDateRangeSelected(it) },
        modifier = modifier,
    )
}

@Composable
fun ManagerContent(
    // Search bar
    query: String,
    onTypingSearch: (String) -> Unit,
    onEnterSearch: (String) -> Unit,
    // Notification list
    notificationList: List<Notification>,
    onRefresh: () -> Unit,
    isRefreshing: Boolean,
    // Apps filter
    onClickAppFilterButton: () -> Unit,
    appList: List<String>,
    appFilterDialogIsShown: Boolean,
    onDismissAppFilterDialog: () -> Unit,
    updateAppFilterSelections: (String) -> Unit,
    onConfirmAppFilter: () -> Unit,
    onCancelAppFilter: () -> Unit,
    // Category filter
    onClickCategoryFilterButton: () -> Unit,
    categoryList: List<String?>,
    categoryFilterDialogIsShown: Boolean,
    onDismissCategoryFilterDialog: () -> Unit,
    updateCategoryFilterSelections: (String?) -> Unit,
    onConfirmCategoryFilter: () -> Unit,
    onCancelCategoryFilter: () -> Unit,
    // Date filter
    onClickDateFilterButton: () -> Unit,
    dateFilterDialogIsShown: Boolean,
    onDismissDateFilterDialog: () -> Unit,
    onDateRangeSelected: (Pair<Long?, Long?>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val focusState = remember { mutableStateOf(false) }

    Column (
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            KeywordsSearchBar(
                query = query,
                onQueryChange = onTypingSearch,
                onSearch = onEnterSearch,
                keyboardController = keyboardController,
                focusRequester = focusRequester,
                focusManager = focusManager,
                focusState = focusState,
            )

            FilterDialog(
                // Apps filter
                onClickAppFilterButton = onClickAppFilterButton,
                appList = appList,
                appFilterDialogIsShown = appFilterDialogIsShown,
                onDismissAppFilterDialog = onDismissAppFilterDialog,
                updateAppFilterSelections = updateAppFilterSelections,
                onConfirmAppFilter = onConfirmAppFilter,
                onCancelAppFilter = onCancelAppFilter,
                // Category filter
                onClickCategoryFilterButton = onClickCategoryFilterButton,
                categoryList = categoryList,
                categoryFilterDialogIsShown = categoryFilterDialogIsShown,
                onDismissCategoryFilterDialog = onDismissCategoryFilterDialog,
                updateCategoryFilterSelections = updateCategoryFilterSelections,
                onConfirmCategoryFilter = onConfirmCategoryFilter,
                onCancelCategoryFilter = onCancelCategoryFilter,
                // Date filter
                onClickDateFilterButton = onClickDateFilterButton,
                dateFilterDialogIsShown = dateFilterDialogIsShown,
                onDismissDateFilterDialog = onDismissDateFilterDialog,
                onDateRangeSelected = onDateRangeSelected
            )
        }

        NotificationCardList(
            notificationList,
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
        )
    }
}


@Composable
fun KeywordsSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: @Composable () -> Unit = { Text("Search") },
    leadingIcon: @Composable (() -> Unit)? = { Icon(Icons.Default.Search, contentDescription = "Search") },
    keyboardController:SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
    focusRequester: FocusRequester = FocusRequester(),
    focusManager: FocusManager = LocalFocusManager.current,
    focusState: MutableState<Boolean> = mutableStateOf(false)
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch(query)
                focusManager.clearFocus()
                keyboardController?.hide()
            }
        ),
        shape = RoundedCornerShape(10.dp),
        leadingIcon = leadingIcon,
        placeholder = placeholder,
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(fontSize = 20.sp),
        modifier = modifier
            .padding(start = 8.dp, end = 8.dp)
            .fillMaxWidth(0.85f)
            .focusRequester(focusRequester)
            .onFocusChanged { focusState.value = it.isFocused }
            .focusable()
        )
}


@Composable
fun FilterDialog(
    // Apps filter
    onClickAppFilterButton: () -> Unit,
    appList: List<String>,
    appFilterDialogIsShown: Boolean,
    onDismissAppFilterDialog: () -> Unit,
    updateAppFilterSelections: (String) -> Unit,
    onConfirmAppFilter: () -> Unit,
    onCancelAppFilter: () -> Unit,
    // Category filter
    onClickCategoryFilterButton: () -> Unit,
    categoryList: List<String?>,
    categoryFilterDialogIsShown: Boolean,
    onDismissCategoryFilterDialog: () -> Unit,
    updateCategoryFilterSelections: (String?) -> Unit,
    onConfirmCategoryFilter: () -> Unit,
    onCancelCategoryFilter: () -> Unit,
    // Date filter
    onClickDateFilterButton: () -> Unit,
    dateFilterDialogIsShown: Boolean,
    onDismissDateFilterDialog: () -> Unit,
    onDateRangeSelected: (Pair<Long?, Long?>) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            IconButton(
                onClick = { expanded = !expanded },
                modifier = Modifier
                    .height(55.dp)
                    .clip(RoundedCornerShape(10.dp))
            ) {
                Icon(
                    painter = painterResource(R.drawable.filter_icon),
                    contentDescription = "Favorite",
                    modifier = Modifier
                        .size(35.dp)
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.clip(RoundedCornerShape(10.dp))
            ) {
                DropdownMenuItem(
                    text = { Text("Apps") },
                    onClick = onClickAppFilterButton
                )
                DropdownMenuItem(
                    text = { Text("Category") },
                    onClick = onClickCategoryFilterButton
                )
                DropdownMenuItem(
                    text = { Text("Date") },
                    onClick = onClickDateFilterButton
                )
                DropdownMenuItem(
                    text = { Text("Clear") },
                    onClick = {/*TODO: Implement the function of Clear button */}
                )
            }

            when {
                appFilterDialogIsShown -> Apps(
                    appList = appList,
                    onDismissRequest = onDismissAppFilterDialog,
                    updateAppFilterSelections = { updateAppFilterSelections(it) },
                    onCancelAppFilter = { onCancelAppFilter() },
                    onConfirmAppFilter = { onConfirmAppFilter() }
                )
                categoryFilterDialogIsShown -> Category(
                    categoryList = categoryList,
                    onDismissRequest = onDismissCategoryFilterDialog,
                    updateCategoryFilterSelections = { updateCategoryFilterSelections(it) },
                    onCancelCategoryFilter = { onCancelCategoryFilter() },
                    onConfirmCategoryFilter = { onConfirmCategoryFilter() }
                )
                dateFilterDialogIsShown -> Date(
                    onDateRangeSelected = { onDateRangeSelected(it) },
                    onDismiss= { onDismissDateFilterDialog() }
                )
            }
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
                val dateString = formatter.format(notification.date)

                NotificationCard(
                    packageName = notification.packageName,
                    title = notification.title,
                    text = notification.text,
                    date = dateString,
                )
                Spacer(modifier = modifier.height(0.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ManagerScreenPreview() {
    val notificationList = listOf(
        Notification(
            rowid = 0,
            packageName = "com.preview.whatever",
            title = "This is a title",
            text = "In Jetpack Compose, you can create a horizontal rule (a horizontal divider or line) " +
                    "using the Divider composable, which is part of the Material design components. " +
                    "The Divider composable allows you to draw a simple line that spans across the " +
                    "width" + " of its parent container.",
            bigText = null,
            category = null,
            date = java.util.Date().time
        ),

        Notification(
            rowid = 0,
            packageName = "com.preview.whatever",
            title = "This is a title",
            text = "In Jetpack Compose, you can create a horizontal rule (a horizontal divider or line) " +
                    "using the Divider composable, which is part of the Material design components. " +
                    "The Divider composable allows you to draw a simple line that spans across the " +
                    "width" + " of its parent container.",
            bigText = null,
            category = null,
            date = java.util.Date().time
        ),
    )

    ManagerContent(
        notificationList = notificationList,
        onRefresh = {},
        onTypingSearch = { _: String -> },
        onEnterSearch = { _: String -> },
        query = "",
        onClickAppFilterButton = {},
        appList = listOf(),
        appFilterDialogIsShown = false,
        onDismissAppFilterDialog = {},
        onClickCategoryFilterButton = {},
        categoryList = listOf(),
        categoryFilterDialogIsShown = false,
        onDismissCategoryFilterDialog = {},
        updateCategoryFilterSelections = {},
        onConfirmCategoryFilter = {},
        onCancelCategoryFilter = {},
        onClickDateFilterButton = {},
        dateFilterDialogIsShown = false,
        onDismissDateFilterDialog = {},
        updateAppFilterSelections = {},
        onConfirmAppFilter = {},
        onCancelAppFilter = {},
        onDateRangeSelected = {},
        isRefreshing = false,
    )
}
