package jp.kusunoki.hacku2022_android.ui.parts

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import jp.kusunoki.hacku2022_android.LocalNavController
import jp.kusunoki.hacku2022_android.R
import jp.kusunoki.hacku2022_android.Screen
import jp.kusunoki.hacku2022_android.data.model.Comment
import jp.kusunoki.hacku2022_android.data.model.HistoryEntity
import jp.kusunoki.hacku2022_android.data.model.YoutubeList
import jp.kusunoki.hacku2022_android.util.Future
import timber.log.Timber

@Composable
fun YouTubeCardRow(videoItemState: Future<YoutubeList> = Future.Proceeding) {
    val navController = LocalNavController.current

    when(videoItemState) {
        is Future.Proceeding -> {

        }
        is Future.Success -> {
            val list = videoItemState.value.items
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                items(list) {video ->
                    val snippet = video.snippet
                    val channelTitle = snippet.channelTitle
                    val title = snippet.title
                    val thumbnail = snippet.thumbnails.medium.url
                    val videoId = video.id.videoId
                    
                    YoutubeCard(
                        channelTitle = channelTitle,
                        title = title,
                        thumbnailPath = thumbnail,
                        onCardClicked = {
                            navController.navigate("${Screen.Video.route}/$videoId")
                        }
                    )
                }
            }
        }
        is Future.Error -> {

        }
    }
}

@Composable
fun YouTubeCardRow() {
    val navController = LocalNavController.current
    val mock = (1..8).map { YouTubeCardMock("いかの塩辛", "Jetpack Compose for Webを触ってみる [Kotlin]", "https://i.ytimg.com/vi/NRDko7XBD7I/mqdefault.jpg", "NRDko7XBD7I") }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(mock) {video ->
            YoutubeCard(
                channelTitle = video.channelTitle,
                title = video.title,
                thumbnailPath = video.thumbnail,
                onCardClicked = {
                    navController.navigate("${Screen.Video.route}/${video.videoId}")
                }
            )
        }
    }
}

data class YouTubeCardMock(val channelTitle: String, val title: String, val thumbnail: String, val videoId: String)

@Composable
fun YoutubeCard(
    channelTitle: String = "",
    title: String = "",
    channelImage: String = "",
    thumbnailPath: String = "",
    onCardClicked: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .height(250.dp)
            .width(215.dp)
            .padding(8.dp)
            .wrapContentSize()
            .clickable(onClick = onCardClicked),
        backgroundColor= MaterialTheme.colors.surface,
        elevation = 8.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            if (thumbnailPath.isNotBlank()) {
                AsyncImage(
                    model = thumbnailPath,
                    contentDescription = null,
                    placeholder = painterResource(R.drawable.image),
                    modifier = Modifier
                        .height(112.5.dp)
                        .width(200.dp),
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.image),
                    contentDescription = stringResource(R.string.samune_image),
                    modifier = Modifier
                        .height(112.5.dp)
                        .width(200.dp),
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Crop
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ){
                if (channelImage.isNotBlank()) {
                    Image(
                        painter = painterResource(id = R.drawable.circle),
                        contentDescription = stringResource(R.string.channel_image),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(4.dp)
                            .size(32.dp)
                            .clip(CircleShape)
                    )
                }
                Text(
                    channelTitle,
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.subtitle1 ,
                    maxLines = 1, // 最大1行に制限する
                    overflow = TextOverflow.Ellipsis // 最大行数を超えた場合に末尾を省略する
                )
            }
            Text(
                title,
                style = MaterialTheme.typography.body2,
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(),
                maxLines = 2, // 最大2行に制限する
                overflow = TextOverflow.Ellipsis // 最大行数を超えた場合に末尾を省略する
            )
        }
    }
}

@Composable
fun LandscapeYoutubeCard(
    title: String = "",
    channelTitle: String = "",
    thumbnailPath: String = "",
    onCardClicked: () -> Unit
) {
    Card(
        shape = RectangleShape,
        modifier = Modifier
            .width(LocalConfiguration.current.screenWidthDp.dp)
            .clickable(onClick = onCardClicked)
            .padding(8.dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 0.dp
    ) {
        Row {
            val padding = 8.dp
            val imageHeight = 112.5.dp
            val imageWidth = 150.dp

            if (thumbnailPath.isNotBlank()) {
                AsyncImage(
                    model = thumbnailPath,
                    contentDescription = null,
                    placeholder = painterResource(R.drawable.image),
                    modifier = Modifier
                        .padding(padding)
                        .height(imageHeight)
                        .width(imageWidth),
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.image),
                    contentDescription = stringResource(R.string.samune_image),
                    modifier = Modifier
                        .padding(padding)
                        .height(imageHeight)
                        .width(imageWidth),
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Crop
                )
            }

            Column {
                Text(
                    title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1, // 最大1行に制限する
                    overflow = TextOverflow.Ellipsis // 最大行数を超えた場合に末尾を省略する
                )
                Text(
                    channelTitle,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    maxLines = 1, // 最大1行に制限する
                    overflow = TextOverflow.Ellipsis // 最大行数を超えた場合に末尾を省略する
                )
            }
        }
    }
}