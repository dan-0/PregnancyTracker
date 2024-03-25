package me.danlowe.pregnancytracker.ui.screen.logscreen.add.view

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.twotone.PhoneAndroid
import androidx.compose.material.icons.twotone.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import kotlinx.collections.immutable.ImmutableList
import me.danlowe.pregnancytracker.R
import me.danlowe.pregnancytracker.ui.screen.logscreen.add.data.AttachmentType

@Composable
fun AddLogAttachments(
  attachments: ImmutableList<String>,
  modifier: Modifier = Modifier,
  addAttachments: (AttachmentType) -> Unit,
) {
  var showAttachmentSourceDialog by rememberSaveable {
    mutableStateOf(false)
  }

  if (showAttachmentSourceDialog) {
    Dialog(onDismissRequest = { showAttachmentSourceDialog = false }) {
      AddLogDialogContent(
        closeDialog = { showAttachmentSourceDialog = false },
        addAttachments = addAttachments,
      )
    }
  }

  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    Text(stringResource(R.string.attachments))
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(AddLogDimens.attachmentSize)
        .horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      attachments.forEach {
        AttachmentBox {
          AsyncImage(
            model = it,
            contentDescription = stringResource(
              R.string.cd_image_attachment,
              Uri.parse(it).lastPathSegment ?: "",
            ),
            contentScale = ContentScale.Crop,
          )
        }
      }

      // Add attachment
      AttachmentBox(
        modifier = Modifier.clickable { showAttachmentSourceDialog = true },
      ) {
        Icon(
          Icons.Default.Add,
          contentDescription = stringResource(R.string.cd_add_image_attachment),
        )
      }
    }
  }
}

@Composable
private fun AddLogDialogContent(
  closeDialog: () -> Unit,
  addAttachments: (AttachmentType) -> Unit,
) {
  Column(
    modifier = Modifier
      .background(
        MaterialTheme.colorScheme.background,
        MaterialTheme.shapes.medium,
      )
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    Text(
      text = stringResource(R.string.add_attachment),
      style = MaterialTheme.typography.titleMedium,
    )
    Text(
      text = stringResource(R.string.where_add_attachment_from),
      style = MaterialTheme.typography.bodyMedium,
    )
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
    ) {
      // Add from camera
      AttachmentBox(
        modifier = Modifier.clickable {
          addAttachments(AttachmentType.Camera)
          closeDialog()
        },
      ) {
        Column(
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          Icon(
            Icons.TwoTone.PhotoCamera,
            contentDescription = stringResource(R.string.cd_add_attachment_from_camera),
          )
          Text(
            text = stringResource(R.string.camera),
            style = MaterialTheme.typography.labelSmall,
          )
        }
      }

      // Add from gallery
      AttachmentBox(
        modifier = Modifier.clickable {
          addAttachments(AttachmentType.MediaPicker)
          closeDialog()
        },
      ) {
        Column(
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          Icon(
            Icons.TwoTone.PhoneAndroid,
            contentDescription = stringResource(R.string.cd_add__attachment_from_local_media),
          )
          Text(
            text = stringResource(R.string.phone),
            style = MaterialTheme.typography.labelSmall,
          )
        }
      }
    }
  }
}
