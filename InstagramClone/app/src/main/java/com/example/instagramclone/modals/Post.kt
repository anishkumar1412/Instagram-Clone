package com.example.instagramclone.modals

data class Post(
    var id: String = "",
    var postUrl: String = "",
    var caption: String = "",
    var uid: String = "",
    var time: String = "",
    var likes: Int = 0,
    val likedBy: MutableList<String> = mutableListOf() // New field to keep track of users who liked the post
) {
    constructor(postUrl: String, caption: String) : this() {
        this.postUrl = postUrl
        this.caption = caption
    }

    constructor(postUrl: String, caption: String, uid: String, time: String, likes: Int) : this() {
        this.postUrl = postUrl
        this.caption = caption
        this.uid = uid
        this.time = time
        this.likes = likes
    }
}
