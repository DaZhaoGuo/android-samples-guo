package com.example.guo.language.kotlin.annotation


// 保持，保存
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
annotation class SerializableJSON()