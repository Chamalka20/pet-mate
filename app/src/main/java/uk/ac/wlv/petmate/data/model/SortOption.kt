package uk.ac.wlv.petmate.data.model

enum class SortOption(val label: String) {
    NONE("None"),
    RATING_HIGH("Rating ↑"),
    PRICE_LOW("Price ↓"),
    PRICE_HIGH("Price ↑"),
    WAITING_TIME("Wait Time ↑")
}