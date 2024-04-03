package org.d3if3114.assessment1.ui.theme.screen

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3114.assessment1.R
import org.d3if3114.assessment1.navigation.Screen
import org.d3if3114.assessment1.ui.theme.theme.Assessment1Theme

@Composable
fun ScreenContent(modifier: Modifier) {
    var keterangan by rememberSaveable { mutableStateOf("") }
    var keteranganError by rememberSaveable { mutableStateOf(false) }

    var tanggal by rememberSaveable { mutableStateOf("") }
    var tanggalError by rememberSaveable { mutableStateOf(false) }

    var nominal by rememberSaveable { mutableStateOf("") }
    var nominalError by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current

    val radioOptions = listOf(
        stringResource(id = R.string.pemasukan),
        stringResource(id = R.string.pengeluaran)
    )
    var kategori by remember { mutableStateOf(radioOptions[0]) }
    var resultMessage by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.intro),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )

        Image(
            painter = painterResource(id = R.drawable.uang),
            contentDescription = "Uang",
            modifier = Modifier.size(200.dp)
        )

        Row(
            modifier = Modifier
                .padding(top = 6.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
        ) {
            radioOptions.forEach { text ->
                GenderOption(
                    label = text,
                    isSelected = kategori == text,
                    modifier = Modifier
                        .selectable(
                            selected = kategori == text,
                            onClick = { kategori = text },
                            role = Role.RadioButton
                        )
                        .weight(1f)
                        .padding(16.dp)
                )
            }
        }

        OutlinedTextField(
            value = keterangan,
            onValueChange = { keterangan = it },
            label = { Text(text = stringResource(R.string.keterangan)) },
            isError = keteranganError,
            trailingIcon = { IconPicker(keteranganError, "") },
            supportingText = { ErrorHint(keteranganError) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = tanggal,
            onValueChange = { tanggal = it },
            label = { Text(text = stringResource(R.string.tanggal)) },
            isError = tanggalError,
            trailingIcon = { IconPicker(tanggalError, "") },
            supportingText = { ErrorHint(tanggalError) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = nominal,
            onValueChange = { nominal = it },
            label = { Text(text = "Nominal") },
            isError = nominalError,
            trailingIcon = { IconPicker(nominalError, "") },
            supportingText = { ErrorHint(nominalError) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    keterangan = ""
                    tanggal = ""
                    nominal = ""
                    resultMessage= ""
                },
                modifier = Modifier.padding(top = 8.dp),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Text(text = "Reset")
            }
            Button(
                onClick = {
                    // Validasi input
                    keteranganError = keterangan.isBlank()
                    tanggalError = tanggal.isBlank()
                    nominalError = nominal.isBlank()

                    // Jika tidak ada error, proses data
                    if (!keteranganError && !tanggalError && !nominalError) {
                        resultMessage =
                            "Keterangan: $keterangan\nTanggal: $tanggal\nNominal: $nominal\nKategori: $kategori"
                    }
                },
                modifier = Modifier.padding(top = 8.dp),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Text(text = stringResource(R.string.catat))
            }
        }
        // Menampilkan pesan hasil
        if (resultMessage.isNotEmpty()) {
            Text(
                text = resultMessage,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            )
            Button(
                onClick = {
                    // Memanggil fungsi shareData
                    shareData(
                        context = context,
                        message = resultMessage
                    )
                },
                modifier = Modifier.padding(top = 8.dp),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Text(text = "Bagikan")
            }
        }
    }
}

@Composable
fun GenderOption (label: String, isSelected: Boolean, modifier: Modifier) {
    Row (
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ){
        RadioButton(selected = isSelected, onClick = null )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

private fun shareData(context: Context, message: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if (shareIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(shareIntent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate(Screen.About.route)
                        }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(R.string.tentang_aplikasi),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { padding ->
        ScreenContent(Modifier.padding(padding))
    }
}

@Composable
fun IconPicker(isError: Boolean, unit: String) {
    if (isError) {
        Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
    } else {
        Text(text = unit)
    }
}

@Composable
fun ErrorHint(isError: Boolean) {
    if (isError) {
        Text(text = stringResource(R.string.input_invalid))
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ScreenPreview() {
    Assessment1Theme {
        MainScreen(rememberNavController())

    }
}