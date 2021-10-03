package com.nulstudio.hit_b02_340;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.os.HandlerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.nulstudio.hit_b02_340.exception.LoginException;
import com.nulstudio.hit_b02_340.mgr.AccountManager;
import com.nulstudio.hit_b02_340.mgr.ClockManager;
import com.nulstudio.hit_b02_340.mgr.JBManager;
import com.nulstudio.hit_b02_340.util.SHA256;

import org.json.JSONException;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    private EditText editUserName, editPassword;
    private CheckBox check;
    private String inviteCode = "";

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());

        check = view.findViewById(R.id.check_login_agreement);
        Button loginButton = view.findViewById(R.id.btn_login),
                registerButton = view.findViewById(R.id.btn_register);
        editUserName = view.findViewById(R.id.edit_login_user_name);
        editPassword = view.findViewById(R.id.edit_login_password);
        ImageView backButton = view.findViewById(R.id.btn_update_back);
        FrameLayout layout = view.findViewById(R.id.frame_login_reg_fragment);

        SpannableStringBuilder textSpannedBuilder1 = new SpannableStringBuilder();
        SpannableString textSpanned11 = new SpannableString(" 同意 ");
        SpannableString textSpanned12 = new SpannableString("用户协议");
        textSpanned12.setSpan(new ForegroundColorSpan(requireActivity().getColor(R.color.btn_primary)),
                0, textSpanned12.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        check.setText(textSpannedBuilder1.append(textSpanned11).append(textSpanned12));

        View.OnClickListener listenerBack = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().finish();
            }
        };
        ((LoginActivity)requireActivity()).backButton.setOnClickListener(listenerBack);

        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    if(editUserName.getText().length() != 0 && editPassword.getText().length() != 0) {
                        loginButton.setEnabled(true);
                    }
                }
                else {
                    loginButton.setEnabled(false);
                }
            }
        });

        editUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() == 0) {
                    loginButton.setEnabled(false);
                }
                else {
                    if(editUserName.getText().length() != 0 && editPassword.getText().length() != 0
                            && check.isChecked()) {
                        loginButton.setEnabled(true);
                    }
                }
            }
        });

        editPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() == 0) {
                    loginButton.setEnabled(false);
                }
                else {
                    if(editUserName.getText().length() != 0 && editPassword.getText().length() != 0
                            && check.isChecked()) {
                        loginButton.setEnabled(true);
                    }
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HitApplication.executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            AccountManager.getInstance().login(requireActivity(),
                                    editUserName.getText().toString(),
                                    SHA256.encrypt(editPassword.getText().toString()));

                        }
                        catch (LoginException e) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(requireActivity(), e.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        catch (JSONException | IOException e) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(requireActivity(), e.toString(),
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                });
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LoginActivity)requireActivity()).title.setText(R.string.txt_register);
                getParentFragmentManager().setFragmentResultListener("register",
                        LoginFragment.this, new FragmentResultListener() {
                            @Override
                            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                                ((LoginActivity)requireActivity()).title.setText(R.string.txt_login);
                                ((LoginActivity)requireActivity()).backButton.setOnClickListener(listenerBack);
                                editUserName.setText(result.getString("userName"));
                                editPassword.setText(result.getString("password"));
                                inviteCode = result.getString("inviteCode");
                                check.setChecked(result.getBoolean("checked"));
                            }
                        });
                getParentFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fragment_reg_trans_in, R.anim.fragment_login_trans_out,
                                R.anim.fragment_login_trans_in, R.anim.fragment_reg_trans_out)
                        .replace(R.id.frame_login_reg_fragment,
                                RegisterFragment.newInstance(editUserName.getText().toString(),
                                        editPassword.getText().toString(), inviteCode, check.isChecked()))
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().finish();
    }
}