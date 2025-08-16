class TvResponse {
  final int? code;
  final String? message;
  final Data? data;

  TvResponse({this.code, this.message, this.data});

  factory TvResponse.fromJson(Map<String, dynamic> json) {
    return TvResponse(
      code: json['code'],
      message: json['message'],
      data: json['data'] != null ? Data.fromJson(json['data']) : null,
    );
  }
}

class Data {
  final List<TvChannel>? channels;
  int? totalElements;
  int? totalPages;
  int? currentPage;
  int? pageSize;

  Data({
    this.channels,
    this.totalElements,
    this.totalPages,
    this.currentPage,
    this.pageSize,
  });

  factory Data.fromJson(Map<String, dynamic> json) {
    return Data(
      channels: json['channels'] != null
          ? List<TvChannel>.from(
              json['channels'].map((x) => TvChannel.fromJson(x)),
            )
          : null,
      totalElements: json['totalElements'],
      totalPages: json['totalPages'],
      currentPage: json['currentPage'],
      pageSize: json['pageSize'],
    );
  }
}

class TvChannel {
  final String? title;
  final String? logo;
  final List<ChannelUrl>? channelUrls;

  TvChannel({this.title, this.logo, this.channelUrls});

  factory TvChannel.fromJson(Map<String, dynamic> json) {
    return TvChannel(
      title: json['title'],
      logo: json['logo'],
      channelUrls: json['channelUrls'] != null
          ? List<ChannelUrl>.from(
              json['channelUrls'].map((x) => ChannelUrl.fromJson(x)),
            )
          : null,
    );
  }
}

class ChannelUrl {
  final String? title;
  final String? url;

  ChannelUrl({this.title, this.url});

  factory ChannelUrl.fromJson(Map<String, dynamic> json) {
    return ChannelUrl(title: json['title'], url: json['url']);
  }
}
