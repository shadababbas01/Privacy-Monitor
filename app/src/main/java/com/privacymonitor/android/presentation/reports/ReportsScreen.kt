package com.privacymonitor.android.presentation.reports

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.privacymonitor.android.R
import com.privacymonitor.android.core.designsystem.CardBackgroundDark
import com.privacymonitor.android.core.designsystem.DeepNavyDark
import com.privacymonitor.android.core.designsystem.PrimaryPurple
import com.privacymonitor.android.core.designsystem.TextPrimaryDark
import com.privacymonitor.android.core.designsystem.TextSecondaryDark

@Composable
fun ReportsScreen(viewModel: ReportsViewModel) {
    val context = LocalContext.current
    val reports by viewModel.reports.collectAsState()
    val exportedPdf by viewModel.exportedPdf.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavyDark)
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.nav_reports),
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimaryDark,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            items(reports) { report ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackgroundDark)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Weekly Privacy Audit",
                                style = MaterialTheme.typography.titleLarge,
                                color = TextPrimaryDark,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Score: ${report.finalScore}",
                                style = MaterialTheme.typography.titleMedium,
                                color = PrimaryPurple,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = report.summaryHinglish,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimaryDark
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { viewModel.exportPdf(report) },
                                modifier = Modifier.weight(1f).height(46.dp),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                            ) {
                                Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = TextPrimaryDark)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(stringResource(id = R.string.export_pdf), color = TextPrimaryDark)
                            }
                        }
                    }
                }
            }
        }

        exportedPdf?.let { file ->
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share Privacy Audit PDF"))
        }
    }
}
