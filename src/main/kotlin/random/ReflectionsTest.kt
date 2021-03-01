package random

import org.reflections.Reflections
import random.sub.SomeInterface
import random.sub.SuperClass

/**
 * @author zp4rker
 */
fun main() {
    val refl = Reflections("random")
    refl.getSubTypesOf(SomeInterface::class.java).forEach(::println)
    refl.getSubTypesOf(SuperClass::class.java).forEach(::println)
}