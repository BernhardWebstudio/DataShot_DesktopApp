#!/usr/bin/env python
import argparse
import json
import sys

try:
    from qreader import QReader
    from cv2 import imread
except ImportError as exc:
    sys.stderr.write(
        f"Missing required Python dependency for QR scanning ({exc}). "
        "Install 'qreader' and 'opencv-python' in your Python environment.\n"
    )
    sys.exit(1)


if __name__ == "__main__":
    argparser = argparse.ArgumentParser(description="QR Code Reader using QReader")
    argparser.add_argument(
        "image_path",
        type=str,
        help="Path to the image file containing the QR code",
    )
    args = argparser.parse_args()

    # Initialize the QReader
    qreader_reader = QReader()
    img = imread(args.image_path)

    qrreader_our = qreader_reader.detect_and_decode(image=img)

    print(json.dumps(qrreader_our, indent=4, ensure_ascii=False))
