/// <reference types="vite/client" />

// 百度地图全局类型声明
declare global {
  interface Window {
    initMap: () => void;
  }

  const BMap: any;
  const BMAP_NAVIGATION_CONTROL_LARGE: any;
}

// 百度地图命名空间
declare namespace BMap {
  class Map {
    constructor(container: string | HTMLElement);
    centerAndZoom(point: Point, zoom: number): void;
    addControl(control: Control): void;
    addOverlay(overlay: Overlay): void;
    setViewport(points: Point[], opts?: any): void;
    getBounds(): Bounds;
    getCenter(): Point;
    getZoom(): number;
    panTo(point: Point): void;
    clearOverlays(): void;
    closeInfoWindow(): void;
  }

  class Point {
    constructor(lng: number, lat: number);
  }

  class Polyline {
    constructor(points: Point[], opts?: PolylineOptions);
  }

  class Marker {
    constructor(point: Point, opts?: MarkerOptions);
    setPosition(point: Point): void;
    openInfoWindow(infoWindow: InfoWindow): void;
    addEventListener(event: string, handler: Function): void;
  }

  class InfoWindow {
    constructor(content: string | HTMLElement, opts?: InfoWindowOptions);
  }

  class NavigationControl {
    constructor(opts?: NavigationControlOptions);
  }

  class ScaleControl {
    constructor(opts?: ScaleControlOptions);
  }

  class OverviewMapControl {
    constructor(opts?: OverviewMapControlOptions);
  }

  class MapTypeControl {
    constructor(opts?: MapTypeControlOptions);
  }

  class Icon {
    constructor(url: string, size: Size, opts?: IconOptions);
  }

  class Size {
    constructor(width: number, height: number);
  }

  interface PolylineOptions {
    strokeColor?: string;
    strokeWeight?: number;
    strokeOpacity?: number;
    strokeStyle?: string;
  }

  interface MarkerOptions {
    icon?: Icon;
  }

  interface InfoWindowOptions {}
  interface NavigationControlOptions {
    type?: any;
  }
  interface ScaleControlOptions {}
  interface OverviewMapControlOptions {}
  interface MapTypeControlOptions {}
  interface IconOptions {
    offset?: Size;
    imageOffset?: Size;
  }

  interface Bounds {}
  interface Control {}
  interface Overlay {}
}

// 环境变量类型声明
interface ImportMetaEnv {
  readonly VITE_APP_TITLE: string
  readonly VITE_API_BASE_URL: string
  readonly VITE_API_TIMEOUT: string
  readonly VITE_BAIDU_MAP_KEY: string
  readonly VITE_APP_MODE: string
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}