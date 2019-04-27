package com.hackathon.ui.comments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.hackathon.Constants
import com.hackathon.R
import com.hackathon.data.model.Comment
import com.hackathon.databinding.CommentsFragmentBinding
import com.hackathon.di.ILogger
import com.hackathon.ui.base.BaseFragment
import com.hackathon.utils.PreferenceUtils
import com.squareup.picasso.Picasso
import org.koin.android.ext.android.inject


class CommentsFragment : BaseFragment<CommentsViewModel>(CommentsViewModel::class) {
    private val logger: ILogger by inject()
    private lateinit var dataBinding: CommentsFragmentBinding
    private var comments: List<Comment> = emptyList()
    private var postId: Int = -1

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        dataBinding = CommentsFragmentBinding.inflate(inflater, container, false)
        dataBinding.lifecycleOwner = this
        dataBinding.lifecycleOwner = this
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        requireActivity().window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = ContextCompat.getColor(requireActivity(), R.color.primaryDarkColor)
        }

        dataBinding.commentsRecyclerView.layoutManager = LinearLayoutManager(context)

        dataBinding.postButton.setOnClickListener {
            if (dataBinding.commentEditText.text.toString().isNotEmpty()) {
                viewModel.commentPost(postId, dataBinding.commentEditText.text.toString())
                dataBinding.commentEditText.setText("")
            }
        }

        val userPhoto = PreferenceUtils.defaultPrefs(requireContext()).getString(Constants.USER_IMAGE, "")
        if (userPhoto.isNullOrEmpty())
            dataBinding.profileImage.setBackgroundResource(R.drawable.fake_profile)
        else
            Picasso.get().load(userPhoto).noFade().error(R.drawable.fake_profile).into(dataBinding.profileImage)

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postId = arguments?.getInt("postId") ?: -1
        viewModel.getComments(postId)
    }

    override fun bindCommands() {
        super.bindCommands()

        viewModel.onCommentsFetched.runWhenFinished(this,
                onSuccess = {
                    this.comments = it.comments
                    dataBinding.commentsRecyclerView.adapter = CommentsAdapter(requireContext(), comments)
                    dataBinding.progressLayout.visibility = View.GONE
                },
                onError = {
                    dataBinding.progressLayout.visibility = View.GONE
                })

        viewModel.onPostCommented.runWhenFinished(this,
                onSuccess = {
                    viewModel.getComments(postId)
                },
                onError = {
                    Toast.makeText(requireContext(), R.string.errorOccurred, Toast.LENGTH_LONG).show()
                })
    }
}
