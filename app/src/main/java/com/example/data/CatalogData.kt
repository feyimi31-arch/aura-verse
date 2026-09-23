package com.example.data

import com.example.model.*

object CatalogData {

    // PRESET BODY PROPORTIONS
    val PRESET_PROPORTIONS = mapOf(
        BodyPreset.PETITE to BodyProportions(
            height = 0.88f, bodyWidth = 0.90f, shoulderWidth = 0.88f, torsoWidth = 0.90f,
            armLength = 0.90f, legLength = 0.90f, armWidth = 0.88f, legWidth = 0.90f
        ),
        BodyPreset.SLIM to BodyProportions(
            height = 1.0f, bodyWidth = 0.92f, shoulderWidth = 0.95f, torsoWidth = 0.92f,
            armLength = 1.0f, legLength = 1.02f, armWidth = 0.92f, legWidth = 0.92f
        ),
        BodyPreset.AVERAGE to BodyProportions(
            height = 1.0f, bodyWidth = 1.0f, shoulderWidth = 1.0f, torsoWidth = 1.0f,
            armLength = 1.0f, legLength = 1.0f, armWidth = 1.0f, legWidth = 1.0f
        ),
        BodyPreset.CURVY to BodyProportions(
            height = 0.98f, bodyWidth = 1.12f, shoulderWidth = 0.98f, torsoWidth = 1.08f,
            armLength = 0.98f, legLength = 0.98f, armWidth = 1.08f, legWidth = 1.16f
        ),
        BodyPreset.TALL to BodyProportions(
            height = 1.18f, bodyWidth = 0.98f, shoulderWidth = 1.08f, torsoWidth = 1.0f,
            armLength = 1.14f, legLength = 1.20f, armWidth = 0.98f, legWidth = 1.02f
        ),
        BodyPreset.ATHLETIC to BodyProportions(
            height = 1.06f, bodyWidth = 1.05f, shoulderWidth = 1.15f, torsoWidth = 1.02f,
            armLength = 1.05f, legLength = 1.06f, armWidth = 1.10f, legWidth = 1.12f
        ),
        BodyPreset.MUSCULAR to BodyProportions(
            height = 1.10f, bodyWidth = 1.18f, shoulderWidth = 1.28f, torsoWidth = 1.18f,
            armLength = 1.08f, legLength = 1.08f, armWidth = 1.25f, legWidth = 1.22f
        ),
        BodyPreset.BROAD to BodyProportions(
            height = 1.12f, bodyWidth = 1.22f, shoulderWidth = 1.35f, torsoWidth = 1.25f,
            armLength = 1.10f, legLength = 1.10f, armWidth = 1.22f, legWidth = 1.20f
        )
    )

    // SKIN TONES
    val SKIN_TONES = listOf(
        0xFFFFF2E6 to "Porcelain Light",
        0xFFFDE4D0 to "Peach Anime",
        0xFFF7D3B5 to "Warm Ivory",
        0xFFE8B896 to "Honey Glow",
        0xFFD49A70 to "Caramel Bronze",
        0xFFB5754C to "Warm Chestnut",
        0xFF824D2E to "Deep Cocoa",
        0xFF5C331D to "Midnight Espresso",
        0xFFF3E8FF to "Starlight Lilac",
        0xFFE0F2FE to "Cyber Cyan Pastel"
    )

    // EYE COLORS
    val EYE_COLORS = listOf(
        0xFF8B5CF6 to "Amethyst Violet",
        0xFF3B82F6 to "Sapphire Blue",
        0xFF06B6D4 to "Cyber Cyan",
        0xFF10B981 to "Emerald Jade",
        0xFFEC4899 to "Sakura Pink",
        0xFFF59E0B to "Amber Gold",
        0xFFEF4444 to "Crimson Flame",
        0xFF1E293B to "Obsidian Black",
        0xFFE2E8F0 to "Starlight Silver"
    )

    // HAIR COLOR PALETTES
    val HAIR_COLORS = listOf(
        0xFF1E1B4B to "Midnight Navy",
        0xFF2A2338 to "Anime Espresso",
        0xFF7C3AED to "Royal Violet",
        0xFFEC4899 to "Neon Magenta",
        0xFF06B6D4 to "Electric Cyan",
        0xFF38BDF8 to "Sky Blue",
        0xFFF43F5E to "Rose Quartz",
        0xFFF59E0B to "Golden Honey",
        0xFFE2E8F0 to "Platinum Silver",
        0xFF10B981 to "Jade Mint",
        0xFFD97706 to "Spicy Ginger",
        0xFF475569 to "Slate Ash"
    )

    // EXPANDABLE HAIRSTYLE SYSTEM
    val HAIRSTYLES = listOf(
        Hairstyle("hair_twin_celestial", "Celestial Twin Tails", HairCategory.TWIN, 0xFF7C3AED, 0xFFEC4899, 0xFF38BDF8),
        Hairstyle("hair_wolf_cut", "Anime Wolf Cut", HairCategory.MEDIUM, 0xFF1E1B4B, 0xFF06B6D4, 0xFFE2E8F0),
        Hairstyle("hair_flowing_silky", "Flowing Starlight Silky", HairCategory.LONG, 0xFFE2E8F0, 0xFF8B5CF6, 0xFF06B6D4),
        Hairstyle("hair_short_spiky", "Cyber Neo Spikes", HairCategory.SHORT, 0xFF2A2338, 0xFF38BDF8, 0xFF7C3AED),
        Hairstyle("hair_modern_bob", "Chic Modern Bob", HairCategory.SHORT, 0xFF2A2338, 0xFFEC4899, 0xFFF59E0B),
        Hairstyle("hair_high_pony", "Idol High Ponytail", HairCategory.SPORT, 0xFF8B5CF6, 0xFF06B6D4, 0xFFE2E8F0),
        Hairstyle("hair_goddess_locs", "Goddess Gold Locs", HairCategory.BRAIDS, 0xFF2A2338, 0xFFF59E0B, 0xFFEC4899),
        Hairstyle("hair_box_braids", "Neon Ombre Braids", HairCategory.BRAIDS, 0xFF1E1B4B, 0xFFEC4899, 0xFF06B6D4),
        Hairstyle("hair_afro_puffs", "Starlight Afro Puffs", HairCategory.CURLY, 0xFF2A2338, 0xFF8B5CF6, 0xFFF59E0B),
        Hairstyle("hair_loose_curls", "Romantic Spiral Curls", HairCategory.CURLY, 0xFFF59E0B, 0xFFEC4899, 0xFFE2E8F0),
        Hairstyle("hair_fantasy_wings", "Valkyrie Wing Tendrils", HairCategory.FANTASY, 0xFFE2E8F0, 0xFF38BDF8, 0xFF7C3AED),
        Hairstyle("hair_twin_buns", "Space Idol Twin Buns", HairCategory.TWIN, 0xFFEC4899, 0xFF7C3AED, 0xFF06B6D4)
    )

    // CLOTHING - TOPS
    val TOPS = listOf(
        ClothingItem("top_cyber_hoodie", "Cyber Neo Crop Hoodie", ClothingCategory.TOPS, "CROP_HOODIE", 0xFF1E1B4B, 0xFF8B5CF6, 0xFF06B6D4),
        ClothingItem("top_academy_blazer", "Aura Academy Blazer", ClothingCategory.TOPS, "BLAZER", 0xFF0F172A, 0xFFE2E8F0, 0xFF8B5CF6),
        ClothingItem("top_street_bomber", "Neo-Tokyo Bomber Jacket", ClothingCategory.TOPS, "BOMBER", 0xFF18181B, 0xFF06B6D4, 0xFFF43F5E),
        ClothingItem("top_idol_frill", "Starlight Idol Frill Blouse", ClothingCategory.TOPS, "FRILL_BLOUSE", 0xFFFFFFFF, 0xFFEC4899, 0xFF8B5CF6),
        ClothingItem("top_pastel_tee", "Oversized Cyber Graphic Tee", ClothingCategory.TOPS, "OVERSIZED_TEE", 0xFFF3E8FF, 0xFF7C3AED, 0xFF06B6D4),
        ClothingItem("top_tech_vest", "Tactical Runner Tech Vest", ClothingCategory.TOPS, "TECH_VEST", 0xFF09090B, 0xFF22C55E, 0xFF06B6D4)
    )

    // CLOTHING - BOTTOMS
    val BOTTOMS = listOf(
        ClothingItem("bot_pleated_skirt", "Academy Pleated Skirt", ClothingCategory.BOTTOMS, "PLEATED_SKIRT", 0xFF1E1B4B, 0xFF8B5CF6, 0xFFFFFFFF),
        ClothingItem("bot_cargo_pants", "Futuristic Tech Cargo Pants", ClothingCategory.BOTTOMS, "CARGO_PANTS", 0xFF0F172A, 0xFF06B6D4, 0xFFF43F5E),
        ClothingItem("bot_ripped_jeans", "Streetwear Washed Denim", ClothingCategory.BOTTOMS, "RIPPED_JEANS", 0xFF1E3A8A, 0xFF60A5FA, 0xFFFFFFFF),
        ClothingItem("bot_cyber_shorts", "Neon Runner Cyber Shorts", ClothingCategory.BOTTOMS, "CYBER_SHORTS", 0xFF18181B, 0xFFEC4899, 0xFF06B6D4),
        ClothingItem("bot_wide_trousers", "Sleek High-Waist Trousers", ClothingCategory.BOTTOMS, "WIDE_TROUSERS", 0xFF0F172A, 0xFFE2E8F0, 0xFF8B5CF6)
    )

    // CLOTHING - FULL OUTFITS
    val FULL_OUTFITS = listOf(
        ClothingItem("outfit_academy_uniform", "Aura Elite Academy Uniform", ClothingCategory.OUTFITS, "ACADEMY_FULL", 0xFF1E1B4B, 0xFFFFFFFF, 0xFF8B5CF6),
        ClothingItem("outfit_celestial_idol", "Celestial Galaxy Idol Dress", ClothingCategory.OUTFITS, "IDOL_DRESS", 0xFF7C3AED, 0xFFEC4899, 0xFF38BDF8),
        ClothingItem("outfit_cyber_runner", "Neo-Tokyo Cyber Runner Suit", ClothingCategory.OUTFITS, "CYBER_RUNNER", 0xFF0F172A, 0xFF06B6D4, 0xFFF43F5E),
        ClothingItem("outfit_cosmic_knight", "Cosmic Starlight Guardian Armor", ClothingCategory.OUTFITS, "COSMIC_ARMOR", 0xFFE2E8F0, 0xFF8B5CF6, 0xFFF59E0B)
    )

    // CLOTHING - SHOES
    val SHOES = listOf(
        ClothingItem("shoe_platform_sneakers", "Aura Platform Kicks", ClothingCategory.SHOES, "PLATFORM_SNEAKERS", 0xFFFFFFFF, 0xFF8B5CF6, 0xFF06B6D4),
        ClothingItem("shoe_combat_boots", "Cyber Combat Buckle Boots", ClothingCategory.SHOES, "COMBAT_BOOTS", 0xFF18181B, 0xFF7C3AED, 0xFF06B6D4),
        ClothingItem("shoe_academy_loafers", "Classic Academy Loafers", ClothingCategory.SHOES, "LOAFERS", 0xFF2A2338, 0xFFE2E8F0, 0xFF8B5CF6),
        ClothingItem("shoe_high_tops", "Neon Glow High-Top Sneakers", ClothingCategory.SHOES, "HIGH_TOPS", 0xFF0F172A, 0xFF06B6D4, 0xFFEC4899)
    )

    // ACCESSORIES
    val ACCESSORIES = listOf(
        AccessoryItem("acc_cat_headphones", "Cyber Cat Ear Headphones", AccessorySlot.HEAD, 0xFF8B5CF6, 0xFF06B6D4),
        AccessoryItem("acc_cyber_visor", "Holographic Cyber Visor", AccessorySlot.FACE, 0xFF06B6D4, 0xFFF43F5E),
        AccessoryItem("acc_angel_wings", "Hologram Starlight Wings", AccessorySlot.BACK, 0xFF38BDF8, 0xFFEC4899),
        AccessoryItem("acc_mecha_backpack", "Aura Grav-Booster Pack", AccessorySlot.BACK, 0xFF1E1B4B, 0xFF06B6D4),
        AccessoryItem("acc_star_choker", "Celestial Moon Choker", AccessorySlot.NECK, 0xFF18181B, 0xFFF59E0B),
        AccessoryItem("acc_halo", "Starlight Orbiting Halo", AccessorySlot.HEAD, 0xFFFBBF24, 0xFFE2E8F0),
        AccessoryItem("acc_anime_glasses", "Stylized Wireframe Anime Glasses", AccessorySlot.FACE, 0xFFE2E8F0, 0xFF8B5CF6),
        AccessoryItem("acc_aura_orbs", "Floating Mystic Sparkle Orbs", AccessorySlot.SPECIAL, 0xFF8B5CF6, 0xFF06B6D4)
    )

    // DISTRICTS
    val DISTRICTS = listOf(
        District(
            id = DistrictId.AURA_CITY,
            name = "Aura City",
            tagline = "The Vibrant Neon Metropolis",
            description = "Explore bustling fashion boulevards, meet friends at trendy rooftop cafes, and cruise through neon-lit cyber plazas.",
            themeColor = 0xFF8B5CF6,
            skyboxColorTop = 0xFF080D1A,
            skyboxColorBottom = 0xFF1E1B4B,
            groundColor = 0xFF111827,
            landmarks = listOf("Neon Plaza Center", "Fashion Boulevard", "Cyber Café Rooftop", "Metropolis Skybridge")
        ),
        District(
            id = DistrictId.AURA_ACADEMY,
            name = "Aura Academy",
            tagline = "Elite Institute of Magic & Tech",
            description = "Attend classes, explore the sprawling library, shoot hoops at the basketball court, or relax in the cherry blossom courtyard.",
            themeColor = 0xFF3B82F6,
            skyboxColorTop = 0xFF0A192F,
            skyboxColorBottom = 0xFF1E3A8A,
            groundColor = 0xFF1E293B,
            landmarks = listOf("Grand Clocktower Gate", "Courtyard Cherry Trees", "Sports Stadium & Hoops", "Astronomy Observatory")
        ),
        District(
            id = DistrictId.AURA_BEACH,
            name = "Aura Beach",
            tagline = "Sun, Waves & Summer Boardwalk",
            description = "Surf glowing ocean waves, play beach volleyball, hang out by umbrella beach houses, and watch the celestial sunset.",
            themeColor = 0xFF06B6D4,
            skyboxColorTop = 0xFF0C4A6E,
            skyboxColorBottom = 0xFF0284C7,
            groundColor = 0xFFFEF08A,
            landmarks = listOf("Star Sand Boardwalk", "Lifeguard Pier", "Volleyball Dunes", "Tropical Juice Lounge")
        ),
        District(
            id = DistrictId.AURA_AMUSEMENT,
            name = "Aura Amusement",
            tagline = "Neon Carnival of Thrills & Games",
            description = "Ride the monumental illuminated Ferris wheel, play arcade challenges, win prizes, and enjoy nighttime fireworks.",
            themeColor = 0xFFEC4899,
            skyboxColorTop = 0xFF180A28,
            skyboxColorBottom = 0xFF581C87,
            groundColor = 0xFF1F1D36,
            landmarks = listOf("Celestial Ferris Wheel", "Neon Arcade Palace", "Carnival Roller Coaster", "Prize Bazaar")
        ),
        District(
            id = DistrictId.MYSTIC_FOREST,
            name = "Mystic Forest",
            tagline = "Bioluminescent Ancient Sanctuary",
            description = "Wander through glowing giant mushrooms, ancient spirit trees, enchanted waterfalls, and sparkling fairy clearings.",
            themeColor = 0xFF10B981,
            skyboxColorTop = 0xFF06281E,
            skyboxColorBottom = 0xFF064E3B,
            groundColor = 0xFF042F2E,
            landmarks = listOf("Great Willow of Light", "Fairy Spring Waterfall", "Giant Mushroom Grove", "Ancient Rune Ring")
        ),
        District(
            id = DistrictId.FANTASY_REALM,
            name = "Fantasy Realm",
            tagline = "Floating Celestial Islands",
            description = "Step onto levitating crystal isles connected by star bridges, explore levitating castles, and touch cosmic nebulae.",
            themeColor = 0xFFA855F7,
            skyboxColorTop = 0xFF140826,
            skyboxColorBottom = 0xFF3B0764,
            groundColor = 0xFF2E1065,
            landmarks = listOf("Floating Citadel", "Crystal Starbridge", "Dragon Peak", "Nebula Sanctum")
        )
    )

    // NPCS
    val NPCS = listOf(
        NPC(
            id = "npc_celeste",
            name = "Celeste",
            role = "Academy Student Council President",
            districtId = DistrictId.AURA_ACADEMY,
            greeting = "Welcome to Aura Academy! Looking to hone your magic tech skills or practice on the basketball court?",
            dialogues = listOf(
                "Aura Academy ranks #1 across all social metaverse realms!",
                "Remember, school uniforms can be customized in the Creator Studio!",
                "Take on the Obby Parkour course near the sports field if you want to test your reflexes."
            ),
            questName = "Academy Tour & Spirit",
            questDesc = "Visit the courtyard and shoot 3 hoops at the basketball court.",
            questRewardCoins = 250,
            questRewardGems = 20,
            posX = 20f,
            posZ = 15f
        ),
        NPC(
            id = "npc_kaito",
            name = "Kaito",
            role = "Celebrity Fashion Designer",
            districtId = DistrictId.AURA_CITY,
            greeting = "Yo! Your style aura is popping today. Want to check out the newest streetwear drop?",
            dialogues = listOf(
                "Fashion is how you project your soul onto the metaverse.",
                "Have you tried publishing your own clothes in Creator Studio? Players earn Aura Gems from sales!",
                "Hoverboards match best with oversized crop hoodies. Fact."
            ),
            questName = "Runway Ready",
            questDesc = "Customize an outfit in Avatar Creator and preview it in 3D.",
            questRewardCoins = 300,
            questRewardGems = 25,
            posX = -18f,
            posZ = -22f
        ),
        NPC(
            id = "npc_maya",
            name = "Maya",
            role = "Beach Lifeguard & Surfer",
            districtId = DistrictId.AURA_BEACH,
            greeting = "Hey there! Waves are glowing bright today. Catch the breeze or grab a tropical drink!",
            dialogues = listOf(
                "The ocean water here is 100% simulated warm starlight.",
                "Watch out for the volleyball tournament tonight!",
                "My celestial dolphin pet always accompanies me when swimming."
            ),
            questName = "Boardwalk Sprinter",
            questDesc = "Run across the beach boardwalk using turbo speed.",
            questRewardCoins = 200,
            questRewardGems = 15,
            posX = 35f,
            posZ = -10f
        ),
        NPC(
            id = "npc_nyx",
            name = "Nyx",
            role = "Forest Sprite Guardian",
            districtId = DistrictId.MYSTIC_FOREST,
            greeting = "Hush... listen to the ancient trees whispering melodies of the starlight.",
            dialogues = listOf(
                "The bioluminescent flora here responds to dance emotes.",
                "Fairy pets love to nestle beside the glowing river stones.",
                "Deep in the grove lies the path to the floating Fantasy Realm."
            ),
            questName = "Whispers of Flora",
            questDesc = "Perform a Dance Emote beside the Great Willow of Light.",
            questRewardCoins = 350,
            questRewardGems = 30,
            posX = -10f,
            posZ = 30f
        )
    )

    // VEHICLES
    val VEHICLES = listOf(
        Vehicle("veh_hoverboard", "Cyber Neo Hoverboard", VehicleType.HOVERBOARD, 1.8f, 1.4f, 0xFF06B6D4, 0, isUnlocked = true),
        Vehicle("veh_sportbike", "Aura Quantum Sports Bike", VehicleType.SPORT_BIKE, 2.5f, 1.8f, 0xFF8B5CF6, 250, isUnlocked = false),
        Vehicle("veh_scooter", "Pastel City Cruiser Scooter", VehicleType.SCOOTER, 1.5f, 1.2f, 0xFFEC4899, 120, isUnlocked = false),
        Vehicle("veh_cruiser", "Celestial Aero Cruiser", VehicleType.CRUISER, 3.2f, 2.0f, 0xFF38BDF8, 500, isUnlocked = false)
    )

    // PETS
    val PETS = listOf(
        Pet("pet_kitsune", "Kira", PetSpecies.CYBER_KITSUNE, 0xFF8B5CF6, 0xFF06B6D4, 3, 95, "Nine-Tail Star Dance", isUnlocked = true),
        Pet("pet_bunny", "Lulu", PetSpecies.CELESTIAL_BUNNY, 0xFFFFFFFF, 0xFFEC4899, 1, 88, "Floating Moon Hop", isUnlocked = false),
        Pet("pet_shiba", "Hachi", PetSpecies.NEON_SHIBA, 0xFFF59E0B, 0xFF06B6D4, 2, 92, "Laser Wag & Backflip", isUnlocked = false),
        Pet("pet_dragon", "Ignis", PetSpecies.STAR_DRAGONLING, 0xFFEF4444, 0xFFFBBF24, 1, 80, "Glitter Flame Breath", isUnlocked = false),
        Pet("pet_neko", "Pixel", PetSpecies.MECHA_CAT, 0xFF06B6D4, 0xFF8B5CF6, 1, 85, "Hologram Heart Purr", isUnlocked = false)
    )

    // EMOTES
    val EMOTES = listOf(
        EmoteAction("emote_wave", "Friendly Wave", "👋", "WAVE"),
        EmoteAction("emote_dance", "Idol Pop Dance", "💃", "DANCE"),
        EmoteAction("emote_heart", "Heart Hands", "💖", "HEART"),
        EmoteAction("emote_cheer", "Hype Cheer", "🎉", "CHEER"),
        EmoteAction("emote_victory", "Peace Victory", "✌️", "VICTORY"),
        EmoteAction("emote_sit", "Chill Sit", "🪑", "SIT"),
        EmoteAction("emote_flex", "Anime Flex", "💪", "FLEX")
    )

    // COMMUNITY MARKETPLACE ITEMS
    val COMMUNITY_MARKETPLACE = listOf(
        CreatorItem("comm_1", "Cyber Blossom Oversized Tee", "SakuraRider", ClothingCategory.TOPS, 45, "SAKURA_GRID", 0xFF1E1B4B, 0xFFEC4899, 0xFF06B6D4, 384, false),
        CreatorItem("comm_2", "Starlight Pleated Skirt", "AuraQueen", ClothingCategory.BOTTOMS, 50, "GALAXY_STARS", 0xFF0F172A, 0xFF8B5CF6, 0xFF38BDF8, 512, false),
        CreatorItem("comm_3", "Neo-Tokyo Idol Gown", "ChronoStylist", ClothingCategory.OUTFITS, 120, "HOLOGRAM_NEON", 0xFF7C3AED, 0xFF06B6D4, 0xFFEC4899, 890, false),
        CreatorItem("comm_4", "Cyberpunk High-Tops", "KicksMaster", ClothingCategory.SHOES, 60, "SOLID", 0xFF111827, 0xFFF43F5E, 0xFF06B6D4, 420, false),
        CreatorItem("comm_5", "Aurora Borealis Hoodie", "SolarFlare", ClothingCategory.TOPS, 75, "AURORA_WAVE", 0xFF064E3B, 0xFF10B981, 0xFF38BDF8, 640, false),
        CreatorItem("comm_6", "Aero Pilot Combat Boots", "MechaAce", ClothingCategory.SHOES, 80, "SOLID", 0xFF1E1B4B, 0xFFF59E0B, 0xFF8B5CF6, 310, false)
    )
}
