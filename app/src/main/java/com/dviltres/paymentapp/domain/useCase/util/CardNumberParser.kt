package com.dviltres.paymentapp.domain.useCase.util

/** Parse credit card number, split it on blocks*/
class CardNumberParser(
    private val number: String,
    private val emptyChar: Char = DEFAULT_EMPTY_CHAR
) {
    lateinit var first: String
    lateinit var second: String
    lateinit var third: String
    lateinit var fourth: String

    init {
        splitCardNumber()
    }

    /** create a block using xxxx format */
    private fun initEmptyBlock() = "".padEnd(BLOCK_SIZE, emptyChar)

    private fun splitCardNumber() {
        first = getBlock(blockNumber = FIRST)
        second = getBlock(blockNumber = SECOND)
        third = getBlock(blockNumber = THIRD)
        fourth = getBlock(blockNumber = FOURTH)
    }

    /** get a block of card number * [1,2,3,4] represents xxxx-xxxx-xxxx-xxxx */

    private fun getBlock(blockNumber: Int): String {
        val length = number.length
        var block = initEmptyBlock()
        val start = (blockNumber - 1) * BLOCK_SIZE
        val end = blockNumber * BLOCK_SIZE

        if (length in start until end) {
            block = number
                .substring(start, length)
                .padEnd(BLOCK_SIZE, emptyChar)
        } else if (number.length >= start) {
            block = number.substring(start, end)
        }

        return block
    }

    companion object {
        private const val DEFAULT_EMPTY_CHAR = 'x'
        private const val BLOCK_SIZE = 4
        private const val FIRST = 1
        private const val SECOND = 2
        private const val THIRD = 3
        private const val FOURTH = 4
    }
}





