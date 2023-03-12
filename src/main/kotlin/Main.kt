import kotlinx.coroutines.*
import kotlin.coroutines.EmptyCoroutineContext
// Задача 1
//fun main() = runBlocking {
//    val job = CoroutineScope(EmptyCoroutineContext).launch {
//        launch {
//            delay(500)
//            println("ok") // <--
//        }
//        launch {
//            delay(500)
//            println("ok")
//        }
//    }
//    delay(100)
//
//    job.cancelAndJoin()
//}

    //Да. Но обе корутины отработают и после произойдет их одновременная отмена
//    Задача №2
//fun main() = runBlocking {
//    val job = CoroutineScope(EmptyCoroutineContext).launch {
//        val child = launch {
//            delay(500)
//            println("ok") // <--
//        }
//        launch {
//            delay(500)
//            println("ok")
//        }
//        delay(100)
//        child.cancel()
//    }
//    delay(100)
//    job.join()
//}
//Нет. команда child.cancel() отменит дочерний корутин.
//Exception Handling
//#1
//    fun main() {
//        with(CoroutineScope(EmptyCoroutineContext)) {
//            try {
//                launch {
//                    throw Exception("something bad happened")
//                }
//            } catch (e: Exception) {
//                e.printStackTrace() // <--
//            }
//        }
//        Thread.sleep(1000)
//    }
//Нет. printStackTrace() не перехватывает исключение в корутине, т.к. try catch вне корутина
//#2
//fun main() {
//    CoroutineScope(EmptyCoroutineContext).launch {
//        try {
//            coroutineScope {
//                throw Exception("something bad happened")
//            }
//        } catch (e: Exception) {
//            e.printStackTrace() // <--
//        }
//    }
//    Thread.sleep(1000)
//}
// Да. Функция coroutineScope позволяет добавить обработку исключений и связывает с родительской корутиной.
//#3
//    fun main() {
//        CoroutineScope(EmptyCoroutineContext).launch {
//            try {
//                supervisorScope {
//                    throw Exception("something bad happened")
//                }
//            } catch (e: Exception) {
//                e.printStackTrace() // <--
//            }
//        }
//        Thread.sleep(1000)
//    }
// Да. SupervisorScope позволяет создавать дочерние корутины, которые могут завершаться независимо от родительской корутины.
//#4
//    fun main() {
//        CoroutineScope(EmptyCoroutineContext).launch {
//            try {
//                coroutineScope {
//                    launch {
//                        delay(500)
//                        throw Exception("something bad happened") // <--
//                    }
//                    launch {
//
//                        throw Exception("something bad happened")
//                    }
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//        Thread.sleep(1000)
//    }
// Нет.Первым придет 2я дочерняя корутина, т.к. в первой имеется задержка.
// Произойдет немедленное прерывание корутины, которое прервет остальные.
//#5
//    fun main() {
//    CoroutineScope(EmptyCoroutineContext).launch {
//        try {
//            supervisorScope {
//                launch {
//                    delay(500)
//                    throw Exception("something bad happened") // <--
//                }
//                launch {
//                    throw Exception("something bad happened")
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace() // <--
//        }
//    }
//    Thread.sleep(1000)
//}
//Первая строка да, т.к. у нас подключена ф-ция supervisorScope и придет после 2й дочерней корутины StackTrace не выпоняется,
// т.к. в родительский корутин ничего не передали. Дочерние корутины завершаются независимо от родительской с помощью supervisorScope.
    //#6
//    fun main() {
//        CoroutineScope(EmptyCoroutineContext).launch {
//            CoroutineScope(EmptyCoroutineContext).launch {
//                launch {
//                    delay(1000)
//                    println("ok") // <--
//                }
//                launch {
//                    delay(500)
//                    println("ok")
//                }
//
//                throw Exception("something bad happened")
//            }
//        }
//        Thread.sleep(1000)
//    }
//Нет. Здесь возникает конкуренция между корутинами и выпоняется Exception, т.к. он придет раньше.
// #7
//    fun main() {
//        CoroutineScope(EmptyCoroutineContext).launch {
//            CoroutineScope(EmptyCoroutineContext + SupervisorJob()).launch {
//                launch {
//                    delay(1000)
//                    println("ok") // <--
//                }
//                launch {
//                    delay(500)
//                    println("ok")
//                }
//
//                throw Exception("something bad happened")
//            }
//        }
//            Thread.sleep(1000)
//    }
//Нет. Здесь возникает конкуренция между корутинами и выпоняется Exception, т.к. он придет раньше.
// Однако не будет распростроняться на родительский coroutine