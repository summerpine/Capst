package com.ljh.Capst.model

//메인화면 리스트에서 쓰일 data들을 모아놓은 Class
data class ContentDTO(var explain : String? = null,
                      var imageUrl : String? = null,
                      var uid : String? = null,
                      var userId : String? = null,
                      var timestamp : Long? = null,
                      var favoriteCount : Int = 0,
                      var commentCount : Int =0,
                      var favorites : MutableMap<String,Boolean> = HashMap()
)
{
    //favorites is 좋아요한 유저들의 UID
data class Comment(var uid : String? = null,
                   var userId : String? = null,
                   var comment : String? = null,
                   var timestamp : Long? = null)
// 회원 정보


}
