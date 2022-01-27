import json
from PIL import Image, ImageDraw, ImageFont

def gen_mc_mod_json_files(c):
    with open("blockstates/wordle_block_{}.json".format(c), "w") as block_states_file:
        """
        {
            "variants": {
                "wordle_state=default": { "model": "wordlemod:block/wordle_block_a" },
                "wordle_state=right": { "model": "wordlemod:block/wordle_block_a_right" },
                "wordle_state=exist": { "model": "wordlemod:block/wordle_block_a_exist" }
            }
        }
        """
        json.dump({"variants": {
            "wordle_state=default": { "model": f"wordlemod:block/wordle_block_{c}"},
            "wordle_state=right": { "model": f"wordlemod:block/wordle_block_{c}_right" },
            "wordle_state=exist": { "model": f"wordlemod:block/wordle_block_{c}_exist" }
        }}, block_states_file)
    for state in ["", "_exist", "_right"]:
        with open("models/block/wordle_block_{}{}.json".format(c, state), "w") as block_file:
            """
            {
                "parent": "block/cube_all",
                "textures": {
                    "all": "wordlemod:wordle_block_a_exist"
                }
            }
            """
            json.dump({"parent": "block/cube_all", 
                "textures": {
                    "all": f"wordlemod:wordle_block_{c}{state}"
                }
            }, block_file)

    with open("models/item/wordle_block_{}_item.json".format(c), "w") as item_file:
        """
        {
            "parent": "wordlemod:block/wordle_block_a"
        }
        """
        json.dump({"parent": f"wordlemod:block/wordle_block_{c}"}, item_file)

GRAY = (0x78, 0x7c, 0x7e)
GREEN = (0x6a, 0xaa, 0x64)
YELLOW = (0xc9, 0xb4, 0x58)
SIZE = 32

if __name__ == "__main__":
    font = ImageFont.load_default()

    for c in "abcdefghijklmnopqrstuvwxyz":
        gen_mc_mod_json_files(c)
        continue
        for (state, color) in zip(["", "_exist", "_right"], [GRAY, YELLOW, GREEN]):
            image = Image.new("RGB", (SIZE, SIZE), color)
            draw = ImageDraw.Draw(image)
            draw.text((SIZE / 2 - 2, SIZE / 2 - 6), c, font=font)
            image.save(f'textures/wordle_block_{c}{state}.png')
