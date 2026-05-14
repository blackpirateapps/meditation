package com.blackpirateapps.pause.domain.model

enum class MeditationIcon(val id: String, val label: String) {
    Lotus("lotus", "Lotus"),
    Moon("moon", "Moon"),
    Breath("breath", "Breath"),
    Waves("waves", "Waves"),
    Forest("forest", "Forest"),
    Focus("focus", "Focus"),
    Gratitude("gratitude", "Gratitude"),
    Sleep("sleep", "Sleep");

    companion object {
        val default = Lotus

        fun fromId(id: String?): MeditationIcon =
            entries.firstOrNull { it.id == id } ?: default
    }
}
