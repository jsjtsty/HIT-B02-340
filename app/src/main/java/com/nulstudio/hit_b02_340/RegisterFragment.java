package com.nulstudio.hit_b02_340;

import android.os.Bundle;

import androidx.core.os.HandlerCompat;
import androidx.fragment.app.Fragment;

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
import android.widget.Toast;

import com.nulstudio.hit_b02_340.exception.LoginException;
import com.nulstudio.hit_b02_340.mgr.AccountManager;
import com.nulstudio.hit_b02_340.util.SHA256;

import org.json.JSONException;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    private EditText editUserName, editPassword, editInviteCode;
    private String userName, password, inviteCode;
    private boolean checked = false;
    private CheckBox check;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance(String userName, String password, String inviteCode,
                                               boolean checked) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString("userName", userName);
        args.putString("password", password);
        args.putString("inviteCode", inviteCode);
        args.putBoolean("checked", checked);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userName = getArguments().getString("userName");
            password = getArguments().getString("password");
            inviteCode = getArguments().getString("inviteCode");
            checked = getArguments().getBoolean("checked");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        Button registerButton = view.findViewById(R.id.btn_reg_register),
                backToLogin = view.findViewById(R.id.btn_reg_back_login);

        Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());

        editUserName = view.findViewById(R.id.edit_reg_user_name);
        editPassword = view.findViewById(R.id.edit_reg_password);
        editInviteCode = view.findViewById(R.id.edit_reg_invite_code);

        check = view.findViewById(R.id.check_reg_agreement);

        editUserName.setText(userName);
        editPassword.setText(password);
        editInviteCode.setText(inviteCode);

        check.setChecked(checked);

        SpannableStringBuilder textSpannedBuilder1 = new SpannableStringBuilder();
        SpannableString textSpanned11 = new SpannableString(" 同意 ");
        SpannableString textSpanned12 = new SpannableString("用户协议");
        textSpanned12.setSpan(new ForegroundColorSpan(requireActivity().getColor(R.color.btn_primary)),
                0, textSpanned12.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        check.setText(textSpannedBuilder1.append(textSpanned11).append(textSpanned12));

        if(editUserName.getText().length() != 0 && editPassword.getText().length() != 0
                && editInviteCode.getText().length() != 0
                && check.isChecked()) {
            registerButton.setEnabled(true);
        }

        View.OnClickListener listenrPreviousFrag = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("userName", editUserName.getText().toString());
                bundle.putString("password", editPassword.getText().toString());
                bundle.putString("inviteCode", editInviteCode.getText().toString());
                bundle.putBoolean("checked", check.isChecked());
                requireActivity().getSupportFragmentManager().
                        setFragmentResult("register", bundle);
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        };

        backToLogin.setOnClickListener(listenrPreviousFrag);

        ((LoginActivity)requireActivity()).backButton.setOnClickListener(listenrPreviousFrag);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() == 0) {
                    registerButton.setEnabled(false);
                }
                else {
                    if(editUserName.getText().length() != 0 && editPassword.getText().length() != 0
                            && editInviteCode.getText().length() != 0
                            && check.isChecked()) {
                        registerButton.setEnabled(true);
                    }
                }
            }
        };

        editUserName.addTextChangedListener(watcher);

        editPassword.addTextChangedListener(watcher);

        editInviteCode.addTextChangedListener(watcher);

        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    if(editUserName.getText().length() != 0 && editPassword.getText().length() != 0
                            && editInviteCode.getText().length() != 0) {
                        registerButton.setEnabled(true);
                    }
                }
                else {
                    registerButton.setEnabled(false);
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HitApplication.executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            AccountManager.getInstance().register(requireActivity(),
                                    editUserName.getText().toString(),
                                    SHA256.encrypt(editPassword.getText().toString()),
                                    editInviteCode.getText().toString());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(requireActivity(),
                                            "注册成功：您的UID是" + AccountManager.getInstance().getUid()
                                                    + "，权限等级是" + AccountManager.getInstance().getPriority(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (LoginException e) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(requireActivity(), e.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (JSONException | IOException e) {
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

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Bundle bundle = new Bundle();
        bundle.putString("userName", editUserName.getText().toString());
        bundle.putString("password", editPassword.getText().toString());
        bundle.putString("inviteCode", editInviteCode.getText().toString());
        bundle.putBoolean("checked", check.isChecked());
        requireActivity().getSupportFragmentManager().
                setFragmentResult("register", bundle);
    }
}