package com.example.guo.language.kotlin.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class JsonField(val key: String = "")
