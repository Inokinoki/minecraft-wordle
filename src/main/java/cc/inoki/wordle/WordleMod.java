package cc.inoki.wordle;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("wordlemod")
public class WordleMod
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public WordleMod() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::preinit);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void preinit(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.OAK_WOOD.getRegistryName());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().options);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.warn("HELLO from Inoki's server starting");
    }

    static WordleBlock[] wordleBlocks = {
        new WordleBlock(AbstractBlock.Properties.of(Material.STONE).speedFactor(0.5f), 97),     // 'a'
        new WordleBlock(AbstractBlock.Properties.of(Material.STONE).speedFactor(0.5f), 98),     // 'b'
        new WordleBlock(AbstractBlock.Properties.of(Material.STONE).speedFactor(0.5f), 99),     // 'c'
        new WordleBlock(AbstractBlock.Properties.of(Material.STONE).speedFactor(0.5f), 100),    // 'd'
        new WordleBlock(AbstractBlock.Properties.of(Material.STONE).speedFactor(0.5f), 101),    // 'e'
        new WordleBlock(AbstractBlock.Properties.of(Material.STONE).speedFactor(0.5f), 102),    // 'f'
        new WordleBlock(AbstractBlock.Properties.of(Material.STONE).speedFactor(0.5f), 103),    // 'g'
        new WordleBlock(AbstractBlock.Properties.of(Material.STONE).speedFactor(0.5f), 104),    // 'h'
        new WordleBlock(AbstractBlock.Properties.of(Material.STONE).speedFactor(0.5f), 105),    // 'i'
        new WordleBlock(AbstractBlock.Properties.of(Material.STONE).speedFactor(0.5f), 106),    // 'j'
        new WordleBlock(AbstractBlock.Properties.of(Material.STONE).speedFactor(0.5f), 107),    // 'k'
        new WordleBlock(AbstractBlock.Properties.of(Material.STONE).speedFactor(0.5f), 108),    // 'l'
        new WordleBlock(AbstractBlock.Properties.of(Material.STONE).speedFactor(0.5f), 109),    // 'm'
        new WordleBlock(AbstractBlock.Properties.of(Material.STONE).speedFactor(0.5f), 110),    // 'n'
        new WordleBlock(AbstractBlock.Properties.of(Material.STONE).speedFactor(0.5f), 111),    // 'o'
        new WordleBlock(AbstractBlock.Properties.of(Material.STONE).speedFactor(0.5f), 112),    // 'p'
        new WordleBlock(AbstractBlock.Properties.of(Material.STONE).speedFactor(0.5f), 113),    // 'q'
        new WordleBlock(AbstractBlock.Properties.of(Material.STONE).speedFactor(0.5f), 114),    // 'r'
        new WordleBlock(AbstractBlock.Properties.of(Material.STONE).speedFactor(0.5f), 115),    // 's'
        new WordleBlock(AbstractBlock.Properties.of(Material.STONE).speedFactor(0.5f), 116),    // 't'
        new WordleBlock(AbstractBlock.Properties.of(Material.STONE).speedFactor(0.5f), 117),    // 'u'
        new WordleBlock(AbstractBlock.Properties.of(Material.STONE).speedFactor(0.5f), 118),    // 'v'
        new WordleBlock(AbstractBlock.Properties.of(Material.STONE).speedFactor(0.5f), 119),    // 'w'
        new WordleBlock(AbstractBlock.Properties.of(Material.STONE).speedFactor(0.5f), 120),    // 'x'
        new WordleBlock(AbstractBlock.Properties.of(Material.STONE).speedFactor(0.5f), 121),    // 'y'
        new WordleBlock(AbstractBlock.Properties.of(Material.STONE).speedFactor(0.5f), 122),    // 'z'
        new WordleBlock(AbstractBlock.Properties.of(Material.STONE).speedFactor(0.5f), 123)     // 'enter'
    };

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.warn("HELLO from Inoki's Register Block");
            for (int i = 0; i < 26; i++) {
                wordleBlocks[i].setRegistryName("wordlemod", "wordle_block_" + String.valueOf((char)('a' + i)));
                blockRegistryEvent.getRegistry().register(wordleBlocks[i]);
            }
            wordleBlocks[26].setRegistryName("wordlemod", "wordle_block_enter");
            blockRegistryEvent.getRegistry().register(wordleBlocks[26]);
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegistryEvent) {
            LOGGER.warn("HELLO from Inoki's Register Item");
            for (int i = 0; i < 26; i++) {
                BlockItem myBlockItem = new BlockItem(wordleBlocks[i], new Item.Properties()
                    .tab(ItemGroup.TAB_BUILDING_BLOCKS));
                myBlockItem.setRegistryName("wordlemod", "wordle_block_" + String.valueOf((char)('a' + i)) + "_item");
                itemRegistryEvent.getRegistry().register(myBlockItem);
            }
            BlockItem myBlockItem = new BlockItem(wordleBlocks[26], new Item.Properties()
                .tab(ItemGroup.TAB_BUILDING_BLOCKS));
            myBlockItem.setRegistryName("wordlemod", "wordle_block_enter_item");
            itemRegistryEvent.getRegistry().register(myBlockItem);
        }
    }
}
