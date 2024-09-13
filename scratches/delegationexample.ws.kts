package main.kotlin

import kotlin.random.Random
import kotlin.reflect.KProperty

class MyDelegate() {
    private var x = 6

    operator fun getValue(myMy: MyMy, property: KProperty<*>): Int {
        println("property.name ${property.name}")
        println("getValue() will return $x")
        return this.x
    }

    operator fun setValue(myMy: MyMy, property: KProperty<*>, i: Int) {
        println("setValue($i)")
        this.x = i
    }
}

class MyMy {
    var y: Int by MyDelegate()
}

var m = MyMy()

m.y
m.y = 666
m.y

class MyAnother() {
    val str: String by lazy {
        println("Computing")
        Random.nextInt(100,999).toString()
    }
}

val m1 = MyAnother()
m1.str
m1.str

val m2 = MyAnother()
m2.str
m2.str
