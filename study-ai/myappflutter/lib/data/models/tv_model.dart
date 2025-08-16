class TvResponse {
  final List<TvChannel>? content;
  final Pageable? pageable;
  final int? totalPages;
  final int? totalElements;
  final bool? last;
  final int? size;
  final int? number;
  final Sort? sort;
  final int? numberOfElements;
  final bool? first;
  final bool? empty;

  TvResponse({
    this.content,
    this.pageable,
    this.totalPages,
    this.totalElements,
    this.last,
    this.size,
    this.number,
    this.sort,
    this.numberOfElements,
    this.first,
    this.empty,
  });

  factory TvResponse.fromJson(Map<String, dynamic> json) {
    return TvResponse(
      content: json['content'] != null
          ? List<TvChannel>.from(json['content'].map((x) => TvChannel.fromJson(x)))
          : null,
      pageable: json['pageable'] != null
          ? Pageable.fromJson(json['pageable'])
          : null,
      totalPages: json['totalPages'],
      totalElements: json['totalElements'],
      last: json['last'],
      size: json['size'],
      number: json['number'],
      sort: json['sort'] != null ? Sort.fromJson(json['sort']) : null,
      numberOfElements: json['numberOfElements'],
      first: json['first'],
      empty: json['empty'],
    );
  }
}

class TvChannel {
  final String? title;
  final String? logo;
  final List<ChannelUrl>? channelUrls;

  TvChannel({
    this.title,
    this.logo,
    this.channelUrls,
  });

  factory TvChannel.fromJson(Map<String, dynamic> json) {
    return TvChannel(
      title: json['title'],
      logo: json['logo'],
      channelUrls: json['channelUrls'] != null
          ? List<ChannelUrl>.from(
              json['channelUrls'].map((x) => ChannelUrl.fromJson(x)))
          : null,
    );
  }
}

class ChannelUrl {
  final String? title;
  final String? url;

  ChannelUrl({
    this.title,
    this.url,
  });

  factory ChannelUrl.fromJson(Map<String, dynamic> json) {
    return ChannelUrl(
      title: json['title'],
      url: json['url'],
    );
  }
}

class Pageable {
  final int? pageNumber;
  final int? pageSize;
  final Sort? sort;
  final int? offset;
  final bool? paged;
  final bool? unpaged;

  Pageable({
    this.pageNumber,
    this.pageSize,
    this.sort,
    this.offset,
    this.paged,
    this.unpaged,
  });

  factory Pageable.fromJson(Map<String, dynamic> json) {
    return Pageable(
      pageNumber: json['pageNumber'],
      pageSize: json['pageSize'],
      sort: json['sort'] != null ? Sort.fromJson(json['sort']) : null,
      offset: json['offset'],
      paged: json['paged'],
      unpaged: json['unpaged'],
    );
  }
}

class Sort {
  final bool? empty;
  final bool? sorted;
  final bool? unsorted;

  Sort({
    this.empty,
    this.sorted,
    this.unsorted,
  });

  factory Sort.fromJson(Map<String, dynamic> json) {
    return Sort(
      empty: json['empty'],
      sorted: json['sorted'],
      unsorted: json['unsorted'],
    );
  }
}