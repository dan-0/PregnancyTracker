package me.danlowe.pregnancytracker.ui.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import kotlinx.collections.immutable.ImmutableList

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun AttachmentImagePager(
  imageUris: ImmutableList<String>,
  modifier: Modifier = Modifier,
  contentScale: ContentScale = ContentScale.FillWidth,
) {
  val pagerState = rememberPagerState(pageCount = { imageUris.size })
  HorizontalPager(state = pagerState) { page ->
    AsyncImage(
      model = imageUris[page],
      contentDescription = null,
      modifier = modifier,
      contentScale = ContentScale.FillWidth,
    )
  }
}