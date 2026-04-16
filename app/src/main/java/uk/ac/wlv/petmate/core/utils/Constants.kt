package uk.ac.wlv.petmate.core.utils

object Constants {

    // ── Environment ───────────────────────────────────────
    const val IS_PRODUCTION = true

    // ── API URLs ──────────────────────────────────────────
    const val LOCAL_URL      = "http://10.0.2.2:7045/"
    const val PRODUCTION_URL = "https://petmateapi-production-fd9d.up.railway.app/"

    // ── Active URL ────────────────────────────────────────
    val BASE_URL = if (IS_PRODUCTION) PRODUCTION_URL else LOCAL_URL


    val VET_SERVICES = listOf(
        "Checkup",
        "Vaccination",
        "Emergency",
        "Surgery",
        "Skin Care",
        "Allergy",
        "Grooming",
        "ICU",
        "Dental Care"

    )

    const val PAGE_SIZE = 10
}