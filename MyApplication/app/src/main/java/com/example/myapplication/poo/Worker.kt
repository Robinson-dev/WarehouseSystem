package com.example.myapplication.poo

class Worker(firstName: String, lastName: String, val jobs: Int=2): Person(firstName,lastName) {
 override fun showActivity()=" algo"
}