package converter

fun main() {
    appRun()
}

fun appRun() {
    while (true) {
        print("Enter what you want to convert (or exit): ")

        //The user input go in a list
        val input: MutableList<String> = readln().split(" ").toMutableList()
        if (input[0] == "exit") break
        if (input.size < 4 ) {
            println("Parse error")
            break
        }
        val numberInput = input[0].toDoubleOrNull()

        //Transform the input in lower case
        for (i in 1..<input.size) {
            input[i] = input[i].lowercase()
        }
        println("")

        if (input[1] == "degrees" || input[1] == "degree") {
            input[1] = "${input[1]} ${input[2]}"
            input.removeAt(2)
        }
        val unitInLower = input[1]
        if (input[3] == "degrees" || input[3] == "degree") {
            input[3] = "${input[3]} ${input[4]}"
            input.removeAt(4)
        }
        val unitOutLower = input[3]

        val unitInEnum = findUnitName(unitInLower)
        val unitOutEnum = findUnitName(unitOutLower)

        if (numberInput == null) println("Parse error")
        else if (unitInEnum.type != unitOutEnum.type || (unitInEnum == UnitEnum.NONE ) or (unitOutEnum == UnitEnum.NONE))
            println("Conversion from ${unitInEnum.plural} to ${unitOutEnum.plural} is impossible")
        else {
            var result = 0.0
            if (unitInEnum.type != "temperature") {
                if (numberInput<0.0) {
                    println("${unitInEnum.type} shouldn't be negative")
                } else {
                    result = numberInput * unitInEnum.rate / unitOutEnum.rate
                    println("$numberInput ${if (numberInput == 1.0) unitInEnum.singular else unitInEnum.plural} is $result ${if (result == 1.0) unitOutEnum.singular else unitOutEnum.plural}")
                }

            } else {
                if (unitInEnum.name == "CELSIUS") {
                    result = when (unitOutEnum.name) {
                        "FAHRENHEIT" -> numberInput * 9 / 5 + 32
                        "KELVIN" -> numberInput + 273.15
                        else -> numberInput
                    }
                }
                if (unitInEnum.name == "FAHRENHEIT") {
                    result = when (unitOutEnum.name) {
                        "CELSIUS" -> (numberInput - 32) * 5 / 9
                        "KELVIN" -> (numberInput + 459.67) * 5 / 9
                        else -> numberInput
                    }
                }
                if (unitInEnum.name == "KELVIN") {
                    result = when (unitOutEnum.name) {
                        "CELSIUS" -> numberInput - 273.15
                        "FAHRENHEIT" -> (numberInput * 9 / 5) - 459.67
                        else -> numberInput
                    }
                }
                println("$numberInput ${if (numberInput == 1.0) unitInEnum.singular else unitInEnum.plural} is $result ${if (result == 1.0) unitOutEnum.singular else unitOutEnum.plural}")
            }
        }
    }
}

fun findUnitName(userUnit: String): UnitEnum {
    for (unitEnum in UnitEnum.entries) if (userUnit == unitEnum.initial || userUnit == unitEnum.singular || userUnit == unitEnum.plural || userUnit == unitEnum.initial2 || userUnit == unitEnum.name2) return unitEnum
    return UnitEnum.NONE
}

enum class UnitEnum(
    val type: String = "",
    val initial: String = "",
    val singular: String = "",
    val plural: String = "???",
    val rate: Double = 0.0,
    val name2: String = "",
    val initial2: String = ""
) {
    METER("Length", "m", "meter", "meters", 1.0),
    MILLIMETER("Length", "mm", "millimeter", "millimeters", 0.001),
    CENTIMETER("Length", "cm", "centimeter", "centimeters", 0.01),
    KILOMETER("Length", "km", "kilometer", "kilometers", 1000.0),
    MILE("Length", "mi", "mile", "miles", 1609.35),
    YARD("Length", "yd", "yard", "yards", 0.9144),
    FOOT("Length", "ft", "foot", "feet", 0.3048),
    INCH("Length", "in", "inch", "inches", 0.0254),

    GRAM("Weight", "g", "gram", "grams", 1.0),
    KILOGRAM("Weight", "kg", "kilogram", "kilograms", 1000.0),
    MILLIGRAM("Weight", "mg", "milligram", "milligrams", 0.001),
    POUND("Weight", "lb", "pound", "pounds", 453.592),
    ONCE("Weight", "oz", "ounce", "ounces", 28.3495),

    CELSIUS("temperature", "c", "degree celsius", "degrees celsius", 0.0, "celsius", "dc"),
    FAHRENHEIT("temperature", "f", "degree fahrenheit", "degrees fahrenheit", 0.0, "fahrenheit", "df"),
    KELVIN("temperature", "k", "kelvin", "kelvins", 0.0),

    NONE
}