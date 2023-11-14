import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

fun main(args: Array<String>) {
    val d = BCryptPasswordEncoder()
    println("Hello World!")

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")

    ///
   val result =  d.matches("111111","")
    println("result=$result")
}