package org.rekhta.ui.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.Autofill
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import org.kafka.auth.R
import org.kafka.common.extensions.alignCenter
import org.kafka.common.extensions.rememberMutableState
import org.kafka.common.extensions.rememberSavableMutableState
import org.kafka.common.image.Icons
import org.kafka.common.simpleClickable
import org.kafka.common.widgets.IconResource
import ui.common.theme.theme.Dimens

@Composable
internal fun LoginWithEmail(
    loginState: LoginState,
    onFocusChanged: (FocusState) -> Unit,
    login: (String, String) -> Unit,
    forgotPassword: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboard = LocalSoftwareKeyboardController.current

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        var username by rememberSavableMutableState(init = { "" })
        var password by rememberSavableMutableState(init = { "" })

        val usernameAutofill = AutofillNode(
            autofillTypes = listOf(AutofillType.EmailAddress),
            onFill = { username = it }
        )
        val passwordAutofill = AutofillNode(
            autofillTypes = listOf(AutofillType.Password),
            onFill = { password = it }
        )

        val autofill = LocalAutofill.current
        LocalAutofillTree.current += usernameAutofill
        LocalAutofillTree.current += passwordAutofill

        LoginTextField(
            modifier = Modifier.autoFill(autofill, usernameAutofill),
            loginTextField = LoginTextField.Username,
            text = username,
            setSearchText = { username = it },
            onFocusChanged = onFocusChanged
        )

        Spacer(modifier = Modifier.height(Dimens.Spacing12))

        LoginTextField(
            modifier = Modifier.autoFill(autofill, passwordAutofill),
            loginTextField = LoginTextField.Password,
            text = password,
            setSearchText = { password = it },
            onFocusChanged = onFocusChanged
        )

        Spacer(modifier = Modifier.height(Dimens.Spacing24))

        LoginButton(keyboard, { login(username, password) }, loginState)

        Text(
            modifier = Modifier
                .align(Alignment.End)
                .simpleClickable {
                    keyboard?.hide()
                    forgotPassword(username)
                }
                .padding(top = Dimens.Spacing20),
            text = stringResource(R.string.forgot_password),
            textDecoration = TextDecoration.Underline,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
private fun LoginButton(
    keyboard: SoftwareKeyboardController?,
    login: () -> Unit,
    loginState: LoginState,
    enabled: Boolean = true,
) {
    Button(
        enabled = enabled,
        shape = RoundedCornerShape(Dimens.Spacing08),
        onClick = {
            keyboard?.hide()
            login()
        }
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
                .padding(vertical = Dimens.Spacing08),
            text = if (loginState.isLogin) stringResource(R.string.login)
            else stringResource(R.string.sign_up),
            style = MaterialTheme.typography.titleSmall.alignCenter()
        )
    }
}

@Composable
internal fun LoginTextField(
    loginTextField: LoginTextField,
    text: String,
    modifier: Modifier = Modifier,
    onFocusChanged: (FocusState) -> Unit = {},
    setSearchText: (String) -> Unit,
) {
    var isPasswordShown by rememberMutableState { false }

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged(onFocusChanged),
        value = text,
        placeholder = {
            Text(
                text = loginTextField.hint,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.secondary
            )
        },
        trailingIcon = {
            if (loginTextField == LoginTextField.Password) {
                IconResource(
                    imageVector = if (isPasswordShown) Icons.EyeOff else Icons.Eye,
                    modifier = Modifier
                        .padding(Dimens.Spacing08)
                        .clickable { isPasswordShown = !isPasswordShown }
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrect = false,
            keyboardType = loginTextField.keyboardType,
            imeAction = loginTextField.imeAction
        ),
        visualTransformation = if (loginTextField == LoginTextField.Password)
            if (isPasswordShown) VisualTransformation.None else loginTextField.visualTransformation
        else loginTextField.visualTransformation,
        onValueChange = { setSearchText(it) },
        textStyle = MaterialTheme.typography.titleSmall,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedLabelColor = MaterialTheme.colorScheme.secondary,
            unfocusedLabelColor = MaterialTheme.colorScheme.secondary,
            containerColor = MaterialTheme.colorScheme.background,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(Dimens.Spacing08)
    )
}

private fun Modifier.autoFill(autofill: Autofill?, autofillNode: AutofillNode) =
    onFocusChanged { focusState ->
        autofill?.run {
            if (focusState.isFocused) {
                requestAutofillForNode(autofillNode)
            } else {
                cancelAutofillForNode(autofillNode)
            }
        }
    }.onGloballyPositioned {
        autofillNode.boundingBox = it.boundsInWindow()
    }

internal enum class LoginTextField(
    val hint: String,
    val keyboardType: KeyboardType,
    val visualTransformation: VisualTransformation,
    val imeAction: ImeAction
) {
    Name("Name", KeyboardType.Text, VisualTransformation.None, ImeAction.Next),
    Username("Email", KeyboardType.Email, VisualTransformation.None, ImeAction.Next),
    Password("Password", KeyboardType.Password, PasswordVisualTransformation(), ImeAction.Done)
}
