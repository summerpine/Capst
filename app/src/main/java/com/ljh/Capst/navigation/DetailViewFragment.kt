package com.ljh.Capst.navigation

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ljh.Capst.R
import com.ljh.Capst.model.AlarmDTO
import com.ljh.Capst.model.ContentDTO
import kotlinx.android.synthetic.main.fragment_detail.view.*
import kotlinx.android.synthetic.main.item_detail.view.*


class DetailViewFragment : Fragment(){
    var firestore : FirebaseFirestore? = null
    var uid : String? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_detail,container,false)
        firestore = FirebaseFirestore.getInstance()
        uid = FirebaseAuth.getInstance().currentUser?.uid
        Log.d(FirebaseAuth.getInstance().currentUser?.uid, "UID가 나온다나온다! !!!");


        view.detailviewfragment_recyclerview.adapter = DetailViewRecyclerViewAdapter()
        view.detailviewfragment_recyclerview.layoutManager = LinearLayoutManager(activity)
        return view
    }
    //애가 전체관리를하는구나!
    inner class DetailViewRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        //지금 댓글 남기는 그 계정이 contentDTOs다.
        var contentDTOs : ArrayList<ContentDTO> = arrayListOf()//contetnDTO타입으로 받고
        var contentUidList : ArrayList<String> = arrayListOf()
        //contentUidList는 해당 게시물의 UID를 지칭한다.
        //contentUid 임의로 배정되는 임의의 수
        init {

            //wheregreater등으로 나오는 화면을 수정할 수 있다.
//            firestore?.collection("images")?.orderBy("timestamp",Query.Direction.DESCENDING )?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            firestore?.collection("images")?.orderBy("favoriteCount",Query.Direction.DESCENDING )?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            contentDTOs.clear()
                contentUidList.clear()
                //Sometimes, This code return null of querySnapshot when it signout
                if(querySnapshot == null) return@addSnapshotListener

                for(snapshot in querySnapshot!!.documents){
                    var item = snapshot.toObject(ContentDTO::class.java)
                    //자동으로 update
                    contentDTOs.add(item!!) //건드는 내용자의 tentDTOs값
                    contentUidList.add(snapshot.id)
                }
                notifyDataSetChanged()
            }
        }
        //뷰홀더와 레이아웃을연결해주는 작업
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder
        {
            var view = LayoutInflater.from(p0.context).inflate(R.layout.item_detail,p0,false)
            return CustomViewHolder(view)
        }

        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

        override fun getItemCount(): Int
        {
            return contentDTOs.size
        }
        //각기 다른 viewHolder에 담긴다.
        override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int)
        {
            var viewholder = (p0 as CustomViewHolder).itemView


            //UserId
            viewholder.detailviewitem_profile_textview.text = contentDTOs!![p1].userId

            //Image
            Glide.with(p0.itemView.context).load(contentDTOs!![p1].imageUrl).into(viewholder.detailviewitem_imageview_content)

            //Explain of content
            viewholder.detailviewitem_explain_textview.text = contentDTOs!![p1].explain

            //likes
            viewholder.detailviewitem_favoritecounter_textview.text = "Likes " + contentDTOs!![p1].favoriteCount

            //comments
            viewholder.detailviewitem_commentcounter_textview.text = "comments " + contentDTOs!![p1].commentCount
       //     viewholder.de
            //This code is when the button is clicked
            viewholder.detailviewitem_favorite_imageview.setOnClickListener{
                favoriteEvent(p1)
                //누르면 올라간다?
            }
            /*viewholder.detailviewitem_comment_imageview.setOnClickListener{
               // favoriteComment(p1)
            }
*/
            //This code is when the page is loaded
            if(contentDTOs!![p1].favorites.containsKey(uid)) //uid가 있으면
            {
                //This is like status
                viewholder.detailviewitem_favorite_imageview.setImageResource(R.drawable.ic_favorite)

            }else{
                //This is unlike status
                viewholder.detailviewitem_favorite_imageview.setImageResource(R.drawable.ic_favorite_border)
            }

            //This code is when the profile image is clicked
            viewholder.detailviewitem_profile_image.setOnClickListener {
                var fragment = UserFragment()
                var bundle = Bundle()
                bundle.putString("destinationUid",contentDTOs[p1].uid)
                bundle.putString("userId",contentDTOs[p1].userId)

                fragment.arguments = bundle
                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.main_content,fragment)?.commit()
            }
            viewholder.detailviewitem_comment_imageview.setOnClickListener { v ->
                var intent = Intent(v.context,CommentActivity::class.java)
                intent.putExtra("contentUid",contentUidList[p1]) //contentUidList이거 로그캣으로 찍어보자.
                intent.putExtra("destinationUid",contentDTOs[p1].uid)
                Log.d(contentUidList[p1], "contentUidList다 !!!");
                Log.d(contentDTOs[p1].userId, "이건 contentDTOs다! ");
                startActivity(intent)
            }
        }

//firestore을 이해해야한다. //내가 누르는거지
        fun favoriteEvent(position : Int)
        {                                                            //contentUidList[position]값들을 얻어낸다.
            var tsDoc = firestore?.collection("images")?.document(contentUidList[position]) //contentUidList[position]에 있는 문서값 가져온다
            firestore?.runTransaction { transaction ->

                Log.d(contentUidList[position], "마 함나와바라!");
                var contentDTO = transaction.get(tsDoc!!).toObject(ContentDTO::class.java)

                //해당게시물의 favorites중 있습니까?
                if(contentDTO!!.favorites.containsKey(uid)){
                    //When the button is clicked
                    contentDTO?.favoriteCount = contentDTO?.favoriteCount - 1
                    contentDTO?.favorites.remove(uid)
                }else{
                    //When the button is not clicked
                    contentDTO?.favoriteCount = contentDTO?.favoriteCount + 1
                    contentDTO?.favorites[uid!!] = true
                    favoriteAlarm(contentDTOs[position].uid!!)
                //contentDTOs현재 댓글남기는에의 uid입니다.
                }
                transaction.set(tsDoc,contentDTO)//변화된 Transacetion을 처리한다.
            }

        } //즉 send버튼을 누를떄 이걸 처리해야한다.

        fun favoriteAlarm(destinationUid : String)
        {
            var alarmDTO = AlarmDTO()
            alarmDTO.destinationUid = destinationUid
            alarmDTO.userId = FirebaseAuth.getInstance().currentUser?.email
            alarmDTO.uid = FirebaseAuth.getInstance().currentUser?.uid
            alarmDTO.kind = 0
            alarmDTO.timestamp = System.currentTimeMillis()
            FirebaseFirestore.getInstance().collection("alarms").document().set(alarmDTO)
        }

    }
}