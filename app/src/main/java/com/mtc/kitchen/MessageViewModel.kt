package com.mtc.kitchen

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mtc.api.APIConstant
import com.mtc.general.BaseViewModel
import com.mtc.models.Message


class MessageViewModel : BaseViewModel() {
    //private var networkState: ConnectionModel? = null

//    companion object {
//        private var _isanim = MutableLiveData<Boolean>()
//        val anim: LiveData<Boolean> get() = _isanim
//        fun setAnim(b: Boolean) {
//            _isanim.postValue(b)
//        }
//    }

//    fun setNetworkState(connectionModel: ConnectionModel) {
//        networkState = connectionModel
//    }

    // Function to remove duplicates from an ArrayList
    fun removeDuplicates(list: ArrayList<Message>): ArrayList<Message> {
        // Create a new ArrayList
        val newList = ArrayList<Message>()
        val oldList = list
        val newlist = list
        oldList.forEach { old ->

            newlist.forEach { new ->


                if (old.messageFrom == new.messageFrom)
                    newList.remove(new)

            }

        }
        // return the new list
        return newList
    }

    fun setMessageToTrue(message: Message) {

        FirebaseDatabase.getInstance().reference.child(APIConstant.CHATS_ROOM_NEW)
            .child(message.tableId!!).child(message.seatId!!).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.hasChildren()) {
                            for (sp in snapshot.children) {
//                                for (_sp in sp.children) {
//                                    for (s in _sp.children) {
                                val message = sp.getValue(Message::class.java)!!
                                message.readChat = true
                                sp.ref.setValue(message)
//                                    }
//                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                }
            )
        //.child(message.pushKey!!).ref.setValue(message)
    }


}
