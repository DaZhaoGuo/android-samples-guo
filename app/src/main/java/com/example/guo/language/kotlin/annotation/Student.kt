package com.example.guo.language.kotlin.annotation

@SerializableJSON
data class Student(
    @JsonField
    val lastName: String,
    @JsonField
    val firstName: String,
    @JsonField("fullName")
    var name: String = "",
    @JsonField("home")
    val address: String,
    @JsonField
    val age: Int,
    val school: String,
) {
    @BeforeSerializable
    fun obtainFullName() {
        name = "$lastName $firstName@"
    }
}
