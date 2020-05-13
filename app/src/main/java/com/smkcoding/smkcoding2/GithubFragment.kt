package com.smkcoding.smkcoding2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.smkcoding.smkcoding2.data.GithubService
import com.smkcoding.smkcoding2.data.apiRequest
import com.smkcoding.smkcoding2.data.httpClient
import com.smkcoding.smkcoding2.util.dismissLoading
import com.smkcoding.smkcoding2.util.showLoading
import com.smkcoding.smkcoding2.util.tampilToast
import kotlinx.android.synthetic.main.fragment_github.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GithubFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_github, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callApiGetGithubUser()
    }

    private fun callApiGetGithubUser() {
        showLoading(context!!, swipeRefreshlayout)

        val httpClient = httpClient()
        val apiRequest = apiRequest<GithubService>(httpClient)

        val call = apiRequest.getUsers()
        call.enqueue(object : Callback<List<GithubUserItem>> {

            override fun onFailure(call: Call<List<GithubUserItem>>, t: Throwable) {
                dismissLoading(swipeRefreshlayout)
            }

            override fun onResponse(
                call: Call<List<GithubUserItem>>,
                response: Response<List<GithubUserItem>>
            ) {
                dismissLoading(swipeRefreshlayout)

                when {
                    response.isSuccessful ->
                        when {
                            response.body()?.size != 0
                            ->
                                tampilGithubUser(response.body()!!)

                            else -> {
                                tampilToast(context!!, "Berhasil")
                            }
                        }

                    else -> {
                        tampilToast(context!!, "Gagal")
                    }
                }
            }
        })
    }

    private fun tampilGithubUser(githubUsers: List<GithubUserItem>) {
        listGithubUser.layoutManager = LinearLayoutManager(context)
        listGithubUser.adapter = GithubUserAdapter(context!!, githubUsers) {
            val githubUser = it
            tampilToast(context!!, githubUser.login)
        }
    }
}