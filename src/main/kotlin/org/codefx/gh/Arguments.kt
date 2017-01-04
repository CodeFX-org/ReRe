package org.codefx.gh

data class Arguments(val token: String)

fun parse(args: Array<String>): Arguments {
    val token = extractToken(args) ?: throw IllegalArgumentException("Specify a token with -t or --token")
    return Arguments(token)
}

private fun extractToken(args: Array<String>): String? {
    val tokenIndex = Integer.max(args.indexOf("-t"), args.indexOf("--token")) + 1
    return if (tokenIndex == 0 || tokenIndex == args.size) null else args[tokenIndex]
}

