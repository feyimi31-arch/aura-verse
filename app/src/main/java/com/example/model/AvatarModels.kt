package com.example.model

enum class GenderTemplate(val label: String) {
    FEMININE("Feminine"),
    MASCULINE("Masculine"),
    NEUTRAL("Neutral")
}

enum class BodyPreset(val label: String) {
    PETITE("Petite"),
    SLIM("Slim"),
    AVERAGE("Average"),
    CURVY("Curvy"),
    TALL("Tall"),
    ATHLETIC("Athletic"),
    MUSCULAR("Muscular"),
    BROAD("Broad")
}

data class BodyProportions(
    val height: Float = 1.0f,          // 0.8f - 1.25f
    val bodyWidth: Float = 1.0f,       // 0.8f - 1.3f
    val shoulderWidth: Float = 1.0f,   // 0.8f - 1.35f
    val torsoWidth: Float = 1.0f,      // 0.8f - 1.3f
    val armLength: Float = 1.0f,       // 0.85f - 1.2f
    val armWidth: Float = 1.0f,        // 0.8f - 1.35f
    val legLength: Float = 1.0f,       // 0.85f - 1.25f
    val legWidth: Float = 1.0f,        // 0.8f - 1.35f
    val handSize: Float = 1.0f,        // 0.8f - 1.25f
    val footSize: Float = 1.0f         // 0.8f - 1.25f
)

enum class EyeShape(val label: String) {
    ANIME_CLASSIC("Classic Anime"),
    DOE_EYES("Doe Eyes"),
    SHARP_ALMOND("Sharp Almond"),
    CAT_EYE("Cat Eye"),
    KIND_EYES("Kind Eyes"),
    SPARKLE_IDOL("Sparkle Idol")
}

enum class EyebrowStyle(val label: String) {
    SOFT_ARCH("Soft Arch"),
    STRAIGHT_ANIME("Straight Anime"),
    BOLD_WARRIOR("Bold Focus"),
    FEATHERY("Feathery")
}

enum class NoseStyle(val label: String) {
    BUTTON_NOSE("Button"),
    STRAIGHT_NOSE("Straight"),
    ANIME_DOT("Anime Dot"),
    REFINED("Refined")
}

enum class MouthStyle(val label: String) {
    CUTE_SMILE("Cute Smile"),
    CONFIDENT_SMIRK("Smirk"),
    GENTLE_GRIN("Gentle Grin"),
    PLAYFUL_POUT("Playful Pout"),
    CAT_SMILE("Cat Smile :3")
}

enum class FaceShape(val label: String) {
    HEART_ANIME("Heart Anime"),
    OVAL_SOFT("Oval Soft"),
    SLIM_CHIN("Slim Chin"),
    ROUND_CUTE("Round Cute"),
    CHISELED("Chiseled")
}

enum class BlushStyle(val label: String) {
    NONE("None"),
    SAKURA_SOFT("Sakura Soft"),
    ROSY_CHEEKS("Rosy Cheeks"),
    SPARKLE_CHEEK("Sparkle Decal"),
    FRECKLES("Natural Freckles"),
    FRECKLES_AND_BLUSH("Freckles & Blush")
}

data class FaceFeatures(
    val skinTone: Long = 0xFFFEE8D6,       // ARGB color
    val eyeShape: EyeShape = EyeShape.ANIME_CLASSIC,
    val eyeSize: Float = 1.0f,             // 0.8f - 1.25f
    val eyeColor: Long = 0xFF8B5CF6,       // Amethyst Violet
    val eyeHighlights: Boolean = true,
    val eyebrowStyle: EyebrowStyle = EyebrowStyle.SOFT_ARCH,
    val eyebrowColor: Long = 0xFF3B2F2F,
    val noseStyle: NoseStyle = NoseStyle.BUTTON_NOSE,
    val mouthStyle: MouthStyle = MouthStyle.CUTE_SMILE,
    val faceShape: FaceShape = FaceShape.HEART_ANIME,
    val blushStyle: BlushStyle = BlushStyle.SAKURA_SOFT,
    val lipGloss: Boolean = true
)

enum class HairCategory(val label: String) {
    SHORT("Short"),
    MEDIUM("Medium"),
    LONG("Long"),
    CURLY("Curly / Textured"),
    BRAIDS("Braids & Locs"),
    TWIN("Twin Tails"),
    FANTASY("Fantasy"),
    SPORT("Sport / Casual")
}

data class Hairstyle(
    val id: String,
    val name: String,
    val category: HairCategory,
    val primaryColor: Long = 0xFF2A2338,
    val secondaryColor: Long = 0xFF8B5CF6,
    val highlightColor: Long = 0xFF06B6D4,
    val hasHighlights: Boolean = true
)

enum class ClothingCategory(val label: String) {
    TOPS("Tops"),
    BOTTOMS("Bottoms"),
    OUTFITS("Full Outfits"),
    SHOES("Shoes")
}

data class ClothingItem(
    val id: String,
    val name: String,
    val category: ClothingCategory,
    val styleKey: String,
    val primaryColor: Long,
    val secondaryColor: Long = 0xFFFFFFFF,
    val accentColor: Long = 0xFFF43F5E,
    val pattern: String = "SOLID"
)

enum class AccessorySlot(val label: String) {
    HEAD("Head"),
    FACE("Face"),
    EARS("Ears"),
    NECK("Neck"),
    BACK("Back"),
    WAIST("Waist"),
    SPECIAL("Special Aura")
}

data class AccessoryItem(
    val id: String,
    val name: String,
    val slot: AccessorySlot,
    val color: Long = 0xFF8B5CF6,
    val secondaryColor: Long = 0xFF06B6D4
)

data class AvatarConfig(
    val template: GenderTemplate = GenderTemplate.FEMININE,
    val preset: BodyPreset = BodyPreset.SLIM,
    val proportions: BodyProportions = BodyProportions(),
    val face: FaceFeatures = FaceFeatures(),
    val hair: Hairstyle = Hairstyle(
        id = "hair_long_twin_tails",
        name = "Celestial Twin Tails",
        category = HairCategory.TWIN,
        primaryColor = 0xFF9333EA,
        secondaryColor = 0xFFEC4899,
        highlightColor = 0xFF38BDF8
    ),
    val top: ClothingItem = ClothingItem(
        id = "top_cyber_hoodie",
        name = "Cyber Neo Crop Hoodie",
        category = ClothingCategory.TOPS,
        styleKey = "CROP_HOODIE",
        primaryColor = 0xFF1E1B4B,
        secondaryColor = 0xFF8B5CF6,
        accentColor = 0xFF06B6D4
    ),
    val bottom: ClothingItem = ClothingItem(
        id = "bot_pleated_skirt",
        name = "Academy Pleated Skirt",
        category = ClothingCategory.BOTTOMS,
        styleKey = "PLEATED_SKIRT",
        primaryColor = 0xFF111827,
        secondaryColor = 0xFF8B5CF6
    ),
    val fullOutfit: ClothingItem? = null,
    val shoes: ClothingItem = ClothingItem(
        id = "shoe_platform_sneakers",
        name = "Aura Platform Kicks",
        category = ClothingCategory.SHOES,
        styleKey = "PLATFORM_SNEAKERS",
        primaryColor = 0xFFFFFFFF,
        secondaryColor = 0xFF8B5CF6,
        accentColor = 0xFF06B6D4
    ),
    val equippedAccessories: List<AccessoryItem> = listOf(
        AccessoryItem("acc_cat_headphones", "Cyber Cat Headphones", AccessorySlot.HEAD, 0xFF8B5CF6, 0xFF06B6D4),
        AccessoryItem("acc_angel_wings", "Hologram Starlight Wings", AccessorySlot.BACK, 0xFF38BDF8, 0xFFF43F5E)
    ),
    val currentPose: String = "IDLE", // IDLE, WALK, RUN, DANCE, WAVE, VICTORY, SIT
    val rotationY: Float = 0f
)
