//package com.example.skinwise.utils

//import android.util.Log
//import com.example.skinwise.model.Product
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore

//class FirestoreFavoritesManager {

  //  private val db = FirebaseFirestore.getInstance()
    //private val auth = FirebaseAuth.getInstance()

    //fun addFavorite(product: Product) {
      //  val uid = auth.currentUser?.uid ?: return
        //db.collection("users")
          //  .document(uid)
            //.collection("favorites")
     //       .document(product.id.toString())
       //     .set(product)
         //   .addOnSuccessListener {
           //     Log.d("Favorites", "Adăugat cu succes")
  //          }
    //        .addOnFailureListener {
      //          Log.e("Favorites", "Eroare la adăugare: ${it.message}")
        //    }
    //}

//    fun removeFavorite(productId: Long) {
  //      val uid = auth.currentUser?.uid ?: return
    //    db.collection("users")
      //      .document(uid)
        //    .collection("favorites")
          //  .document(productId.toString())
  //          .delete()
    //        .addOnSuccessListener {
      //          Log.d("Favorites", "Șters cu succes")
 //           }
   //         .addOnFailureListener {
     //           Log.e("Favorites", "Eroare la ștergere: ${it.message}")
       //     }
   // }

 //   fun getFavoriteProducts(callback: (List<Product>) -> Unit) {
   //     val uid = auth.currentUser?.uid ?: return callback(emptyList())
     //   db.collection("users")
       //     .document(uid)
         //   .collection("favorites")
   //         .get()
     //       .addOnSuccessListener { snapshot ->
       //         val products = snapshot.documents.mapNotNull { it.toObject(Product::class.java) }
         //       callback(products)
  //          }
    //        .addOnFailureListener {
      //          callback(emptyList())
 //           }
   // }

//    fun getFavoriteIds(callback: (Set<Long>) -> Unit) {
//        val uid = auth.currentUser?.uid ?: return callback(emptySet())
//        db.collection("users")
 //           .document(uid)
 //           .collection("favorites")
 //           .get()
 //           .addOnSuccessListener { snapshot ->
 //               val ids = snapshot.documents.mapNotNull { it.id.toLongOrNull() }.toSet()
 //               callback(ids)
 //           }
 //           .addOnFailureListener {
  //              callback(emptySet())
   //         }
  //  }
//}
