package com.jetpack.rowdeletionanimation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.jetpack.rowdeletionanimation.ui.theme.RowDeletionAnimationTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@ExperimentalMaterialApi
@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RowDeletionAnimationTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = "Animation with Row Deletion",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            )
                        }
                    ) {
                        RowDeletionAnimation()
                    }
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun RowDeletionAnimation() {
    var listItem by remember { mutableStateOf(
        listOf(
            ListItem(
                1,
                "Make it Easy",
                "Here we should learn Jetpack Compose..."
            ),
            ListItem(
                2,
                "Make it Easy",
                "Here we should learn Jetpack Compose..."
            ),
            ListItem(
                3,
                "Make it Easy",
                "Here we should learn Jetpack Compose..."
            ),
            ListItem(
                4,
                "Make it Easy",
                "Here we should learn Jetpack Compose..."
            ),
            ListItem(
                5,
                "Make it Easy",
                "Here we should learn Jetpack Compose..."
            ),
            ListItem(
                6,
                "Make it Easy",
                "Here we should learn Jetpack Compose..."
            ),
            ListItem(
                7,
                "Make it Easy",
                "Here we should learn Jetpack Compose..."
            ),
            ListItem(
                8,
                "Make it Easy",
                "Here we should learn Jetpack Compose..."
            ),
            ListItem(
                9,
                "Make it Easy",
                "Here we should learn Jetpack Compose..."
            ),
            ListItem(
                10,
                "Make it Easy",
                "Here we should learn Jetpack Compose..."
            )
        )
    ) }

    LazyColumn {
        items(listItem.size) { index ->
            AnimatedVisibility(
                visible = listItem[index].isVisible,
                exit = fadeOut(
                    animationSpec = TweenSpec(200, 200, FastOutLinearInEasing)
                )
            ) {
                ListItemDetail(listItem[index]) {
                    listItem = changeListItemVisibility(listItem, listItem[index])
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun ListItemDetail(listItem: ListItem, onDeleteRow: (ListItem) -> Unit) {
    val iconSize = (-68).dp
    val swipeableState = rememberSwipeableState(0)
    val iconPx = with(LocalDensity.current) { iconSize.toPx() }
    val anchors = mapOf(0f to 0, iconPx to 1)
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ ->
                    FractionalThreshold(0.5f)
                },
                orientation = Orientation.Horizontal
            )
            .background(Color.Red)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterEnd)
                .padding(end = 10.dp)
        ) {
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        onDeleteRow(listItem)
                    }
                },
                modifier = Modifier
                    .align(Alignment.Center)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete item",
                    tint = Color.White
                )
            }
        }

        AnimatedVisibility(
            visible = listItem.isVisible,
            exit = slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = TweenSpec(200, 0, FastOutLinearInEasing)
            )
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .offset {
                        IntOffset(swipeableState.offset.value.roundToInt(), 0)
                    }
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(Color.White)
                    .clickable { }
            ) {
                val (titleText, contentText, divider) = createRefs()

                Text(
                    text = "${listItem.title} ${listItem.id}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .constrainAs(titleText) {
                            top.linkTo(parent.top, margin = 12.dp)
                            start.linkTo(parent.start, margin = 18.dp)
                            end.linkTo(parent.end, margin = 18.dp)
                            width = Dimension.fillToConstraints
                        }
                )

                Text(
                    text = listItem.content,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .constrainAs(contentText) {
                            bottom.linkTo(parent.bottom, margin = 12.dp)
                            start.linkTo(parent.start, margin = 18.dp)
                            end.linkTo(parent.end, margin = 18.dp)
                            width = Dimension.fillToConstraints
                        }
                )

                Divider(
                    modifier = Modifier
                        .constrainAs(divider) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.fillToConstraints
                        },
                    thickness = 1.dp,
                    color = Color.Gray
                )
            }
        }
    }
}

fun changeListItemVisibility(
    listItem: List<ListItem>,
    removedListItem: ListItem
): List<ListItem> {
    return listItem.map { currentItem ->
        if (currentItem.id == removedListItem.id) {
            currentItem.copy(isVisible = false)
        } else {
            currentItem
        }
    }
}


























