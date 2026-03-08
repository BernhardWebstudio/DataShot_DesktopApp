import CoreGraphics
import Foundation
import ImageIO
import Vision

struct BoundingBox: Codable {
    let x: Double
    let y: Double
    let width: Double
    let height: Double
}

struct Detection: Codable {
    let rawValue: String
    let format: String
    let boundingBox: BoundingBox
}

private func area(_ d: Detection) -> Double {
    max(0.0, d.boundingBox.width) * max(0.0, d.boundingBox.height)
}

private func center(_ d: Detection) -> (Double, Double) {
    (
        d.boundingBox.x + (d.boundingBox.width / 2.0),
        d.boundingBox.y + (d.boundingBox.height / 2.0)
    )
}

private func iou(_ lhs: Detection, _ rhs: Detection) -> Double {
    let x1 = max(lhs.boundingBox.x, rhs.boundingBox.x)
    let y1 = max(lhs.boundingBox.y, rhs.boundingBox.y)
    let x2 = min(lhs.boundingBox.x + lhs.boundingBox.width,
                 rhs.boundingBox.x + rhs.boundingBox.width)
    let y2 = min(lhs.boundingBox.y + lhs.boundingBox.height,
                 rhs.boundingBox.y + rhs.boundingBox.height)

    let iw = max(0.0, x2 - x1)
    let ih = max(0.0, y2 - y1)
    if iw <= 0.0 || ih <= 0.0 {
        return 0.0
    }

    let intersection = iw * ih
    let union = area(lhs) + area(rhs) - intersection
    if union <= 0.0 {
        return 0.0
    }
    return intersection / union
}

private func closeCenter(_ lhs: Detection, _ rhs: Detection) -> Bool {
    let (lx, ly) = center(lhs)
    let (rx, ry) = center(rhs)
    let dx = lx - rx
    let dy = ly - ry
    return (dx * dx + dy * dy) <= (40.0 * 40.0)
}

private func deduplicate(_ detections: [Detection]) -> [Detection] {
    var merged: [Detection] = []

    for candidate in detections {
        var matchedIndex: Int?
        for i in merged.indices {
            let existing = merged[i]
            if candidate.rawValue == existing.rawValue &&
                (iou(candidate, existing) > 0.10 || closeCenter(candidate, existing)) {
                matchedIndex = i
                break
            }
        }

        if let idx = matchedIndex {
            if area(candidate) > area(merged[idx]) {
                merged[idx] = candidate
            }
        } else {
            merged.append(candidate)
        }
    }

    return merged.sorted {
        if abs($0.boundingBox.y - $1.boundingBox.y) > 1.0 {
            return $0.boundingBox.y < $1.boundingBox.y
        }
        return $0.boundingBox.x < $1.boundingBox.x
    }
}

private func supportedSymbologies() -> [VNBarcodeSymbology] {
    let request = VNDetectBarcodesRequest()
    do {
        return try request.supportedSymbologies()
    } catch {
        return [.qr]
    }
}

private func selectedSymbologies(allSymbologies: Bool) -> [VNBarcodeSymbology] {
    let supported = supportedSymbologies()
    if allSymbologies {
        return supported
    }

    let preferredRaw = Set([
        VNBarcodeSymbology.qr.rawValue,
        "VNBarcodeSymbologyMicroQR"
    ])
    let preferred = supported.filter { preferredRaw.contains($0.rawValue) }
    return preferred.isEmpty ? [.qr] : preferred
}

private func detectIn(
    _ image: CGImage,
    symbologies: [VNBarcodeSymbology],
    mapOffsetX: Double,
    mapOffsetY: Double
) -> [Detection] {
    let request = VNDetectBarcodesRequest()
    request.symbologies = symbologies

    let handler = VNImageRequestHandler(cgImage: image, options: [:])
    do {
        try handler.perform([request])
    } catch {
        return []
    }

    let width = Double(image.width)
    let height = Double(image.height)
    var output: [Detection] = []

    if let results = request.results {
        for item in results {
            guard let payload = item.payloadStringValue,
                  !payload.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty
            else {
                continue
            }

            let box = item.boundingBox
            let x = mapOffsetX + (box.origin.x * width)
            let y = mapOffsetY + ((1.0 - box.origin.y - box.height) * height)
            let w = box.width * width
            let h = box.height * height

            output.append(
                Detection(
                    rawValue: payload,
                    format: item.symbology.rawValue,
                    boundingBox: BoundingBox(x: x, y: y, width: w, height: h)
                )
            )
        }
    }

    return output
}

private func tileRects(imageWidth: Int, imageHeight: Int) -> [CGRect] {
    let w = Double(imageWidth)
    let h = Double(imageHeight)
    var rects: [CGRect] = []
    let specs: [(Int, Int, Double)] = [
        (2, 2, 0.20),
        (3, 3, 0.20)
    ]

    for (cols, rows, overlap) in specs {
        let baseTileW = w / Double(cols)
        let baseTileH = h / Double(rows)
        let overlapX = baseTileW * overlap
        let overlapY = baseTileH * overlap

        for row in 0..<rows {
            let y0 = max(0.0, (Double(row) * baseTileH) - (row == 0 ? 0.0 : overlapY / 2.0))
            let y1 = min(h, (Double(row + 1) * baseTileH) + (row == rows - 1 ? 0.0 : overlapY / 2.0))

            for col in 0..<cols {
                let x0 = max(0.0, (Double(col) * baseTileW) - (col == 0 ? 0.0 : overlapX / 2.0))
                let x1 = min(w, (Double(col + 1) * baseTileW) + (col == cols - 1 ? 0.0 : overlapX / 2.0))

                let rect = CGRect(
                    x: floor(x0),
                    y: floor(y0),
                    width: max(1.0, ceil(x1 - x0)),
                    height: max(1.0, ceil(y1 - y0))
                )
                rects.append(rect)
            }
        }
    }

    return rects
}

private func focusedNeighborRect(image: CGImage, anchor: Detection) -> CGRect {
    let imageW = Double(image.width)
    let imageH = Double(image.height)
    let ax = anchor.boundingBox.x
    let ay = anchor.boundingBox.y
    let aw = anchor.boundingBox.width
    let ah = anchor.boundingBox.height

    let x = min(imageW - 1.0, max(0.0, ax + (aw * 0.65)))
    let y = max(0.0, ay - (ah * 0.70))
    let width = min(imageW - x, max(aw * 2.4, imageW * 0.22))
    let height = min(imageH - y, max(ah * 2.4, imageH * 0.22))

    return CGRect(x: floor(x), y: floor(y), width: ceil(width), height: ceil(height))
}

private func findImagePath(_ arguments: [String]) -> String? {
    for arg in arguments.dropFirst() {
        if arg.hasPrefix("--") {
            continue
        }
        return arg
    }
    return nil
}

private func emit(_ detections: [Detection]) {
    let encoder = JSONEncoder()
    if let data = try? encoder.encode(detections),
       let output = String(data: data, encoding: .utf8) {
        print(output)
    } else {
        print("[]")
    }
}

guard let imagePath = findImagePath(CommandLine.arguments) else {
    print("[]")
    exit(0)
}

let imageURL = URL(fileURLWithPath: imagePath)

guard let imageSource = CGImageSourceCreateWithURL(imageURL as CFURL, nil),
      let cgImage = CGImageSourceCreateImageAtIndex(imageSource, 0, nil)
else {
    print("[]")
    exit(0)
}

let allSymbologies = CommandLine.arguments.contains("--all-symbologies")
let symbologies = selectedSymbologies(allSymbologies: allSymbologies)

var detections = detectIn(cgImage, symbologies: symbologies, mapOffsetX: 0.0, mapOffsetY: 0.0)

for tile in tileRects(imageWidth: cgImage.width, imageHeight: cgImage.height) {
    guard let cropped = cgImage.cropping(to: tile) else {
        continue
    }
    detections.append(contentsOf: detectIn(
        cropped,
        symbologies: symbologies,
        mapOffsetX: Double(tile.origin.x),
        mapOffsetY: Double(tile.origin.y)
    ))
}

if let anchor = detections.max(by: { area($0) < area($1) }) {
    let focus = focusedNeighborRect(image: cgImage, anchor: anchor)
    if let cropped = cgImage.cropping(to: focus) {
        detections.append(contentsOf: detectIn(
            cropped,
            symbologies: symbologies,
            mapOffsetX: Double(focus.origin.x),
            mapOffsetY: Double(focus.origin.y)
        ))
    }
}

emit(deduplicate(detections))
