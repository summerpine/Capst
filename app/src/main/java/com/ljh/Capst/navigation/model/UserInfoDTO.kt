package com.ljh.Capst.model

data class UserInfoPrivateDTO (var uid : String? = null,
                           var userEmail : String? = null,
                           var userName : String? = null,
                           var userMobile : String? = null,
                           var userAddress : String? = null
)
data class UserInfoYoutuberDTO(var uid : String? = null,
                           var userEmail : String? = null,
                           var userName : String? = null,
                           var userMobile : String? = null,
                           var userAddress : String? = null,
                           // 구글계정
                           var userGoogleAccount : String ? = null
)
data class UserInfoCompanyDTO(var uid : String? = null,
                          var userEmail : String? = null,
                          var userName : String? = null,
                          var userMobile : String? = null,
                          var userAddress : String? = null,
                          // 사업자 등록번호
                          var userRegistrationNumber : String? = null
)
