package com.example.fingerprint;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.FragmentActivity;

import java.security.KeyStore;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.crypto.Cipher;

public class MainActivity extends AppCompatActivity {

    private KeyStore keyStore;

    private static final String KEY_NAME = "SLAFingerPrint";
    private Cipher cipher;
    private AppCompatTextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Executor executor = Executors.newSingleThreadExecutor();

        FragmentActivity activity = this;

        final BiometricPrompt biometricPrompt = new BiometricPrompt(activity, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    // user clicked negative button
                } else {
                    //TODO: Called when an unrecoverable error has been encountered and the operation is complete.
                }
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                //TODO: Called when a biometric is recognized.
                showDialog("Success","Finger Print verified");

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                //TODO: Called when a biometric is valid but not recognized.
                showDialog("Failure","Finger Print not matched");
            }
        });

        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Set the title to display.")
                .setSubtitle("Set the subtitle to display.")
                .setDescription("Set the description to display")
                .setNegativeButtonText("cancel")
                .build();

        findViewById(R.id.authenticate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);
            }
        });
    }

    private void showDialog(String title, String msg) {
        textView = (AppCompatTextView) findViewById(R.id.errorText);
        textView.setText(title+"::"+msg);
    }
}

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//            // Initializing both Android Keyguard Manager and Fingerprint Manager
//            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
//
//            BiometricPrompt fingerprintManager = (BiometricPrompt) getSystemService(FINGERPRINT_SERVICE);
//
//
//            textView = (AppCompatTextView) findViewById(R.id.errorText);
//
//
//            // Check whether the device has a Fingerprint sensor.
//            if(!fingerprintManager.){
//                /**
//                 * An error message will be displayed if the device does not contain the fingerprint hardware.
//                 * However if you plan to implement a default authentication method,
//                 * you can redirect the user to a default authentication activity from here.
//                 * Example:
//                 * Intent intent = new Intent(this, DefaultAuthenticationActivity.class);
//                 * startActivity(intent);
//                 */
//                textView.setText("Your Device does not have a Fingerprint Sensor");
//            }else {
//                // Checks whether fingerprint permission is set on manifest
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
//                    textView.setText("Fingerprint authentication permission not enabled");
//                }else{
//                    // Check whether at least one fingerprint is registered
//                    if (false) {
//                        textView.setText("Register at least one fingerprint in Settings");
//                    }else{
//                        // Checks whether lock screen security is enabled or not
//                        if (!keyguardManager.isKeyguardSecure()) {
//                            textView.setText("Lock screen security not enabled in Settings");
//                        }else{
//                            generateKey();
//
//
//                            if (cipherInit()) {
//                                BiometricPrompt.CryptoObject cryptoObject = new BiometricPrompt.CryptoObject(cipher);
//                                FingerprintHandler helper = new FingerprintHandler(this);
//                                helper.startAuth(fingerprintManager, cryptoObject);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    @TargetApi(Build.VERSION_CODES.M)
//    protected void generateKey() {
//        try {
//            keyStore = KeyStore.getInstance("AndroidKeyStore");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        KeyGenerator keyGenerator;
//        try {
//            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
//        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
//            throw new RuntimeException("Failed to get KeyGenerator instance", e);
//        }
//
//
//        try {
//            keyStore.load(null);
//            keyGenerator.init(new
//                    KeyGenParameterSpec.Builder(KEY_NAME,
//                    KeyProperties.PURPOSE_ENCRYPT |
//                            KeyProperties.PURPOSE_DECRYPT)
//                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
//                    .setUserAuthenticationRequired(true)
//                    .setEncryptionPaddings(
//                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
//                    .build());
//            keyGenerator.generateKey();
//        } catch (NoSuchAlgorithmException |
//                InvalidAlgorithmParameterException
//                | CertificateException | IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//    @TargetApi(Build.VERSION_CODES.M)
//    public boolean cipherInit() {
//        try {
//            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
//        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
//            throw new RuntimeException("Failed to get Cipher", e);
//        }
//
//
//        try {
//            keyStore.load(null);
//            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
//                    null);
//            cipher.init(Cipher.ENCRYPT_MODE, key);
//            return true;
//        } catch (KeyPermanentlyInvalidatedException e) {
//            return false;
//        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
//            throw new RuntimeException("Failed to init Cipher", e);
//        }
//    }
//}
