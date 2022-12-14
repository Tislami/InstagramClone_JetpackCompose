package com.zeroone.instagramclone_jetpackcompose.domain.use_case.user

data class UserUseCase (
    val getUser: GetUser,
    val setUser: SetUser,
    val follow: Follow,
    val unFollow: UnFollow,
    val getFollow: GetFollow
)