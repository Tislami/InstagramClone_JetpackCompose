package com.zeroone.instagramclone_jetpackcompose.domain.use_case.post

data class PostUseCase (
    val setPost: SetPost,
    val setPostPhoto: SetPostPhoto,
    val getUserPosts: GetUserPosts,
    val getPosts: GetPosts
)