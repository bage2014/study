import 'package:http/http.dart' as http;
import 'dart:convert';

class LoggingClient extends http.BaseClient {
  final http.Client _inner;

  LoggingClient(this._inner);

  @override
  Future<http.StreamedResponse> send(http.BaseRequest request) async {
    print('Request: ${request.method} ${request.url}');
    print('Headers: ${request.headers}');
    if (request is http.Request) {
      print('Body: ${request.body}');
    }

    final response = await _inner.send(request);
    final http.Response decodedResponse = await http.Response.fromStream(response);
    final responseBody = utf8.decode(decodedResponse.bodyBytes);
    print('Response status: ${response.statusCode}');
    print('Response body: $responseBody');

    return http.StreamedResponse(Stream.fromIterable([utf8.encode(responseBody)]), response.statusCode, headers: response.headers);
  }
}