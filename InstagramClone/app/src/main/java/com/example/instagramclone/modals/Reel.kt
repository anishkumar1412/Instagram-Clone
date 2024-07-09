package com.example.instagramclone.modals

class Reel {
    var reelUrl:String=""
    var captionReel:String=""
    var profileLink:String?=null
    constructor()
    constructor(reelUrl: String, captionReel: String) {
        this.reelUrl = reelUrl
        this.captionReel = captionReel
    }
    constructor(reelUrl: String, captionReel: String,profileLink:String) {
        this.reelUrl = reelUrl
        this.captionReel = captionReel
        this.profileLink= profileLink
    }
}