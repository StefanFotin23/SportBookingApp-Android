package com.example.sportbookingapp.backend_classes

class Reservation(
    var date: String,
  //  private var timestamp: String,
    var endingHour: Int,
    var fieldId: String,
    var price: Double,
    var startingHour: Int,
    var status: String = ""
) {

}
