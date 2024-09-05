package org.rekhta.ui.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import org.kafka.auth.R
import org.kafka.common.extensions.rememberMutableState
import org.kafka.common.image.Icons
import org.kafka.common.simpleClickable
import org.kafka.common.widgets.IconResource
import org.kafka.ui.components.material.PrimaryButton
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
        var username by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue(""))
        }
        var password by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue(""))
        }

        val usernameAutofill = AutofillNode(
            autofillTypes = listOf(AutofillType.EmailAddress),
            onFill = { username = TextFieldValue(it, TextRange(it.length)) }
        )
        val passwordAutofill = AutofillNode(
            autofillTypes = listOf(AutofillType.Password),
            onFill = { password = TextFieldValue(it, TextRange(it.length)) }
        )

        val autofill = LocalAutofill.current
        LocalAutofillTree.current += usernameAutofill
        LocalAutofillTree.current += passwordAutofill

        LoginTextField(
            modifier = Modifier.autoFill(autofill, usernameAutofill),
            loginTextField = LoginTextField.Username,
            text = username,
            onValueChange = { username = it },
            onFocusChanged = onFocusChanged
        )

        Spacer(modifier = Modifier.height(Dimens.Spacing12))

        LoginTextField(
            modifier = Modifier.autoFill(autofill, passwordAutofill),
            loginTextField = LoginTextField.Password,
            text = password,
            onValueChange = { password = it },
            onFocusChanged = onFocusChanged
        )

        Spacer(modifier = Modifier.height(Dimens.Spacing24))

        LoginButton(keyboard, { login(username.text, password.text) }, loginState)

        Text(
            modifier = Modifier
                .align(Alignment.End)
                .simpleClickable {
                    keyboard?.hide()
                    forgotPassword(username.text)
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
    PrimaryButton(
        text = if (loginState.isLogin) stringResource(R.string.login) else stringResource(R.string.sign_up),
        enabled = enabled,
    ) {
        keyboard?.hide()
        login()
    }
}

@Composable
internal fun LoginTextField(
    loginTextField: LoginTextField,
    text: TextFieldValue,
    modifier: Modifier = Modifier,
    onFocusChanged: (FocusState) -> Unit = {},
    onValueChange: (TextFieldValue) -> Unit,
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
            autoCorrectEnabled = false,
            keyboardType = loginTextField.keyboardType,
            imeAction = loginTextField.imeAction
        ),
        visualTransformation = if (loginTextField == LoginTextField.Password)
            if (isPasswordShown) VisualTransformation.None else loginTextField.visualTransformation
        else loginTextField.visualTransformation,
        onValueChange = { onValueChange(it) },
        textStyle = MaterialTheme.typography.titleSmall,
        colors = OutlinedTextFieldDefaults.colors(
            focusedLabelColor = MaterialTheme.colorScheme.secondary,
            unfocusedLabelColor = MaterialTheme.colorScheme.secondary,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            focusedContainerColor = MaterialTheme.colorScheme.background,
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
    Username("Email", KeyboardType.Email, VisualTransformation.None, ImeAction.Next),
    Password("Password", KeyboardType.Password, PasswordVisualTransformation(), ImeAction.Done)
}
