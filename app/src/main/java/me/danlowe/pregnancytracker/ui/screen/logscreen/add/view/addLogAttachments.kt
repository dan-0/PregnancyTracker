package me.danlowe.pregnancytracker.ui.screen.logscreen.add.view

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.PhoneAndroid
import androidx.compose.material.icons.twotone.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
  addAttachments: (AttachmentType) -> Unit,
  modifier: Modifier = Modifier,
  deleteAttachment: (String) -> Unit,
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

  var uriForDeletion by rememberSaveable {
    mutableStateOf<String?>(null)
  }

  if (uriForDeletion != null) {
    Dialog(onDismissRequest = { uriForDeletion = null }) {
      DeleteUriDialogContent(uriForDeletion!!, deleteAttachment) {
        uriForDeletion = null
      }
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
        .height(AddLogDimens.attachmentSize + 8.dp)
        .horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      attachments.forEach {
        Box {
          AttachmentBox(
            modifier = Modifier.align(Alignment.Center),
          ) {
            AsyncImage(
              model = it,
              contentDescription = stringResource(
                R.string.cd_image_attachment,
                Uri.parse(it).lastPathSegment ?: "",
              ),
              contentScale = ContentScale.Crop,
            )
          }
          Box(
            modifier = Modifier
              .padding(top = 2.dp, end = 2.dp)
              .align(Alignment.TopEnd)
              .background(MaterialTheme.colorScheme.background, CircleShape)
              .clickable {
                uriForDeletion = it
              },
            contentAlignment = Alignment.Center,
          ) {
            Icon(
              Icons.TwoTone.Delete,
              contentDescription = stringResource(R.string.cd_delete_attachment),
              tint = MaterialTheme.colorScheme.error,
            )
          }
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
private fun DeleteUriDialogContent(
  uriForDeletion: String,
  deleteAttachment: (String) -> Unit,
  closeDialog: () -> Unit,
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
      text = stringResource(R.string.delete_attachment),
      style = MaterialTheme.typography.titleMedium,
    )
    Text(
      text = stringResource(R.string.delete_this_attachment),
      style = MaterialTheme.typography.bodyMedium,
    )
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
    ) {
      Button(onClick = { closeDialog() }) {
        Text(stringResource(R.string.cancel))
      }
      Button(
        onClick = {
          deleteAttachment(uriForDeletion)
          closeDialog()
        },
        colors = ButtonDefaults.buttonColors().copy(
          containerColor = MaterialTheme.colorScheme.error,
        ),
      ) {
        Text(stringResource(R.string.delete_attachment))
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
