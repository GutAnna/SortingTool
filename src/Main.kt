import java.io.File
import java.util.Scanner

open class Elements() {
    companion object {
        private val inputList = mutableListOf<Any>()
        private fun countOccurs(max: Any) = inputList.count { it == max }

        private fun readData(param: String, inputFile: String): MutableList<Any> {
            if (inputFile.isNotEmpty()) {
                val dataFile = File(inputFile)
                val one = ""
                when (param) {
                    "long" -> try {
                        val data = dataFile.readText().trim().split(" ")
                        for (one in data) {
                            inputList.add(one.toInt())
                        }
                    } catch (e: NumberFormatException) {
                        println("\"$one\" is not a long. It will be skipped.")
                    }
                    "line" -> inputList.addAll(dataFile.readLines())
                    else -> inputList.addAll(dataFile.readText().trim().split(" "))

                }
            } else {
                val scan = Scanner(System.`in`)
                lateinit var newInt: String
                while (scan.hasNext()) {
                    when (param) {
                        "long" -> try {
                            newInt = scan.next()
                            inputList.add(newInt.toInt())
                        } catch (e: NumberFormatException) {
                            println("\"$newInt\" is not a long. It will be skipped.")
                        }
                        "line" -> inputList.add(scan.nextLine())
                        else -> inputList.add(scan.next())
                    }
                }
            }
            return inputList
        }

        private fun sorArray(arr: Array<Pair<Any, Int>>): Array<Pair<Any, Int>> {
            var fl = true
            while (fl) {
                fl = false
                for (i in 0 until arr.size - 1) {
                    if (arr[i].second > arr[i + 1].second) {
                        val a = arr[i]
                        arr[i] = arr[i + 1]
                        arr[i + 1] = a
                        fl = true
                    }
                }
            }
            return arr
        }

        private fun sortByCount(dataType: String): Array<Pair<Any, Int>> {
            val elementsMap = mutableMapOf<Any, Int>()
            for (el in inputList) {
                elementsMap[el] = countOccurs(el)
            }
            var elementsArr: Array<Pair<Any, Int>> = if (dataType == "long") {
                elementsMap.toSortedMap(compareBy { it.toString().toInt() }).toList().toTypedArray()
            } else elementsMap.toSortedMap(compareBy { it.toString() }).toList().toTypedArray()

            return sorArray(elementsArr)
        }

        private fun sortByNature(dataType: String): List<Any> {
            return if (dataType == "long") inputList.sortedBy { it.toString().toInt() } else {
                inputList.sortedBy { it.toString() }
            }
        }


        fun printInfo(dataType: String, sortingType: String, inputFile: String, outFile: String) {
            readData(dataType, inputFile)
            val total = inputList.size
            val data = mutableListOf<String>()

            data.add("Total Numbers: $total.\n")
            if (sortingType == "byCount") {
                var sortedData = sortByCount(dataType)
                for (item in sortedData) {
                    data.add("${item.first}: ${item.second} time(s), ${item.second * 100 / total}%\n")
                }
            } else {
                var sortedData = sortByNature(dataType)
                data.add("Sorted data: ")
                if (dataType == "line") {
                    data.add("\n")
                    for (item in sortedData) println(item)
                } else {
                    data.add(sortedData.joinToString(separator = " "))
                }
            }

            if (outFile.isNotEmpty()) {
                val dataFile = File(outFile)
                dataFile.writeText(data.joinToString(""))
            }
            else print(data.joinToString(""))
        }
    }
}


fun main(args: Array<String>) {
    var dataType = "word"
    var sortingType = "nature"
    var inputFile = ""
    var outFile = ""
    var i = 0
    while (i in args.indices) {
        try {
            when (args[i]) {
                "-sortingType" -> {
                    sortingType =
                        if (args[i + 1].first() == '-') throw Exception("No sorting type defined!") else args[i + 1]
                    i++
                }
                "-dataType" -> {
                    dataType = if (args[i + 1].first() == '-') throw Exception("No data type defined!") else args[i + 1]
                    i++
                }
                "-inputFile" -> {
                    inputFile = if (args[i + 1].first() == '-') throw Exception("No input file defined!") else args[i + 1]
                    i++
                }
                "-outputFile" -> {
                    outFile = if (args[i + 1].first() == '-') throw Exception("No out file defined!") else args[i + 1]
                    i++
                }
                else -> println("\"${args[i]}\" is not a valid parameter. It will be skipped.")
            }
        } catch (e: IndexOutOfBoundsException) {
            println("No ${args[i].substringBefore("Type").substring(1)} type defined!")
        } catch (e: Exception) {
            println(e.message)
        }
        i++
    }
    Elements.printInfo(dataType, sortingType, inputFile, outFile)
}


