package com.app.wisebuyer.privacyPolicy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.app.wisebuyer.R


class PrivacyPolicyFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(
            R.layout.fragment_privacy_policy, container, false
        )

        val webViewPrivacyPolicy: WebView = view.findViewById(R.id.web_view)
        val privacyPolicyContent = getString(R.string.privacy_policy_content)

        webViewPrivacyPolicy.loadDataWithBaseURL(null, privacyPolicyContent, "text/html", "utf-8", null);

        return view
    }
}